import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.FileItem;
import model.Rule;
import service.AppLogger;
import service.FileMover;
import service.FileScanner;
import service.RollbackService;
import service.RuleEngine;

public class Main {
    public static void main(String[] args) {

        Path baseFolder = Paths.get("/Users/calvi/Downloads");
        Path logFile = baseFolder.resolve("logs/file-organizer.log");

        AppLogger logger = new AppLogger(logFile);
        FileScanner scanner = new FileScanner();
        RuleEngine ruleEngine = new RuleEngine();
        FileMover mover = new FileMover(logger);
        RollbackService rollbackService = new RollbackService(logger);

        Scanner input = new Scanner(System.in);

        System.out.print("Choose mode: organize or rollback? ");
        String mode = input.nextLine().trim().toLowerCase();

        try {
            if (mode.equals("rollback")) {
                rollbackService.rollbackMoves(logFile);
                return;
            }

            // RULES (priority order)
            List<Rule> rules = List.of(
                    new Rule(null, "resume", null, baseFolder.resolve("Resumes")),

                    new Rule(List.of("jpg", "jpeg", "png"), "screenshot", null, baseFolder.resolve("Screenshots")),

                    new Rule(
                            List.of("java", "py", "js", "ts", "html", "css", "sql", "c", "cpp", "h", "hpp", "r"),
                            null,
                            null,
                            baseFolder.resolve("Code")
                    ),

                    new Rule(List.of("xlsx", "csv"), null, null, baseFolder.resolve("Sheets")),

                    new Rule(List.of("jpg", "jpeg", "png"), null, null, baseFolder.resolve("Pictures")),
                    
                    new Rule(List.of("mp4", "mkv", "avi"), null, null, baseFolder.resolve("Videos")),

                    new Rule(null, null, 730, baseFolder.resolve("Old"))
            );

            List<FileItem> files = scanner.scanFolder(baseFolder);

            // FILTER OUT DESTINATION FOLDERS (IMPORTANT)
            files = files.stream()
                    .filter(file -> file.getPath().getParent().equals(baseFolder))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Resumes")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Videos")))
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

            int moveCount = 0;

            for (Map.Entry<FileItem, Path> entry : preview.entrySet()) {
                FileItem file = entry.getKey();
                Path destination = entry.getValue();

                if (destination != null) {
                    System.out.println(file.getPath() + " -> " + destination);
                    moveCount++;
                } else {
                    System.out.println(file.getPath() + " -> no matching rule");
                    logger.logSkipped(file.getPath(), "No matching rule");
                }
            }

            if (moveCount == 0) {
                System.out.println("\nNo files matched any rules. Nothing to move.");
                return;
            }

            System.out.println("\nProceed with moving " + moveCount + " file(s)? (yes/no): ");
            String response = input.nextLine().trim().toLowerCase();

            if (!response.equals("yes")) {
                System.out.println("Operation cancelled.");
                return;
            }

            System.out.println("\nMoving files (limit = 25 for safety):\n");

            int movedCount = 0;

            for (Map.Entry<FileItem, Path> entry : preview.entrySet()) {
                FileItem file = entry.getKey();
                Path destination = entry.getValue();
            
                if (destination != null) {
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
            System.out.println("Error: " + e.getMessage());
        }
    }
}