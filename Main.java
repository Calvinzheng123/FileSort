import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.FileItem;
import model.Rule;
import service.FileMover;
import service.FileScanner;
import service.RuleEngine;

public class Main {
    public static void main(String[] args) {
        FileScanner scanner = new FileScanner();
        FileMover mover = new FileMover();
        RuleEngine ruleEngine = new RuleEngine();

        Path baseFolder = Paths.get("/Users/calvi/Test-Photos");

        List<Rule> rules = List.of(
                new Rule(null, "resume", baseFolder.resolve("Resumes")),

                new Rule(List.of("jpg", "jpeg", "png"), "screenshot", baseFolder.resolve("Screenshots")),

                new Rule(List.of("jpg", "jpeg", "png"), null, baseFolder.resolve("Pictures")),

                new Rule(List.of("xlsx", "csv"), null, baseFolder.resolve("Sheets"))
        );

        try {
            List<FileItem> files = scanner.scanFolder(baseFolder);

            files = files.stream()
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Resumes")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Screenshots")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Pictures")))
                    .filter(file -> !file.getPath().startsWith(baseFolder.resolve("Sheets")))
                    .toList();

            Map<FileItem, Path> preview = ruleEngine.generatePreview(files, rules);

            System.out.println("Files found: " + files.size());
            System.out.println();
            System.out.println("Preview of file organization:");

            int moveCount = 0;

            for (Map.Entry<FileItem, Path> entry : preview.entrySet()) {
                FileItem file = entry.getKey();
                Path destination = entry.getValue();

                if (destination != null) {
                    System.out.println(file.getPath() + " -> " + destination);
                    moveCount++;
                } else {
                    System.out.println(file.getPath() + " -> no matching rule");
                }
            }

            if (moveCount == 0) {
                System.out.println();
                System.out.println("No files matched any rules. Nothing to move.");
                return;
            }

            System.out.println();
            System.out.print("Proceed with moving " + moveCount + " file(s)? (yes/no): ");

            Scanner input = new Scanner(System.in);
            String response = input.nextLine().trim().toLowerCase();

            if (!response.equals("yes")) {
                System.out.println("Operation cancelled.");
                return;
            }

            System.out.println();
            System.out.println("Moving files:");

            int movedCount = 0;

            for (Map.Entry<FileItem, Path> entry : preview.entrySet()) {
                FileItem file = entry.getKey();
                Path destination = entry.getValue();

                if (destination != null) {
                    mover.moveFile(file, destination);
                    movedCount++;
                }
            }

            System.out.println();
            System.out.println("Done. Moved " + movedCount + " file(s).");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}