import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.FileItem;
import model.Rule;
import service.AppLogger;
import service.ConfigLoader;
import service.FileMover;
import service.FileScanner;
import service.RollbackService;
import service.RuleEngine;

public class Main {
    public static void main(String[] args) {
        try {
            Path configPath = Path.of("config.properties");
            ConfigLoader configLoader = new ConfigLoader(configPath);

            Path baseFolder = configLoader.getBaseFolder();
            int moveLimit = configLoader.getMoveLimit();
            Path logFile = baseFolder.resolve("logs/file-organizer.log");

            AppLogger logger = new AppLogger(logFile);
            FileScanner scanner = new FileScanner();
            RuleEngine ruleEngine = new RuleEngine();
            FileMover mover = new FileMover(logger);
            RollbackService rollbackService = new RollbackService(logger);

            Scanner input = new Scanner(System.in);

            System.out.print("Choose mode: organize or rollback? ");
            String mode = input.nextLine().trim().toLowerCase();

            if (mode.equals("rollback")) {
                rollbackService.rollbackMoves(logFile);
                return;
            }

            List<Rule> rules = configLoader.loadRules(baseFolder);
            List<FileItem> files = scanner.scanFolder(baseFolder);

            files = files.stream()
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Resumes")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Screenshots")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Code")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Sheets")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Pictures")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Old")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("logs")))
                    .toList();

            Map<FileItem, Path> preview = ruleEngine.generatePreview(files, rules);

            System.out.println("\nFiles found: " + files.size());
            System.out.println("\nPreview of file organization:\n");

            int matchedCount = 0;

            for (Map.Entry<FileItem, Path> entry : preview.entrySet()) {
                FileItem file = entry.getKey();
                Path destination = entry.getValue();

                if (destination != null) {
                    System.out.println(file.getPath() + " -> " + destination);
                    matchedCount++;
                } else {
                    System.out.println(file.getPath() + " -> no matching rule");
                    logger.logSkipped(file.getPath(), "No matching rule");
                }
            }

            if (matchedCount == 0) {
                System.out.println("\nNo files matched any rules. Nothing to move.");
                return;
            }

            System.out.print("\nProceed with moving " + matchedCount + " file(s)? (yes/no): ");
            String response = input.nextLine().trim().toLowerCase();

            if (!response.equals("yes")) {
                System.out.println("Operation cancelled.");
                return;
            }

            System.out.println("\nMoving files:\n");

            int movedCount = 0;

            for (Map.Entry<FileItem, Path> entry : preview.entrySet()) {
                FileItem file = entry.getKey();
                Path destination = entry.getValue();

                if (destination != null) {
                    if (moveLimit != -1 && movedCount >= moveLimit) {
                        System.out.println("\nReached move limit of " + moveLimit + ". Stopping early.");
                        break;
                    }

                    try {
                        mover.moveFile(file, destination);
                        movedCount++;
                    } catch (IOException e) {
                        System.out.println("Failed to move: " + file.getPath());
                        logger.logError(file.getPath(), e.getMessage());
                    }
                }
            }

            System.out.println("\nDone. Moved " + movedCount + " file(s).");
            System.out.println("Log written to: " + logFile);

        } catch (IOException e) {
            System.out.println("Configuration or file error: " + e.getMessage());
        }
    }
}