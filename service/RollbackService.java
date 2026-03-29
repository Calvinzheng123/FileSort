package service;

import model.MoveRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
Handles undoing file movements using data from the log file.

This class reads previously logged MOVE operations and reverses them,
restoring files to their original locations.

Key behavior:
- parses log entries to reconstruct move operations
- processes rollback in reverse order (last-in-first-out)
- safely handles missing files and conflicts

Safety features:
- skips files that no longer exist
- avoids overwriting existing files
- recreates original directories if needed

design:
- depends on AppLogger output format
- enables recovery from incorrect file organization runs
*/

public class RollbackService {

    private final AppLogger logger;

    public RollbackService(AppLogger logger) {
        this.logger = logger;
    }

    public void rollbackMoves(Path logFilePath) throws IOException {
        List<MoveRecord> records = readMoveRecords(logFilePath);

        if (records.isEmpty()) {
            System.out.println("No move records found. Nothing to roll back.");
            return;
        }

        Collections.reverse(records);

        int rollbackCount = 0;

        for (MoveRecord record : records) {
            Path originalSource = record.getSourcePath();
            Path currentLocation = record.getDestinationPath();

            if (!Files.exists(currentLocation)) {
                System.out.println("Skipping missing file: " + currentLocation);
                logger.logSkipped(currentLocation, "Rollback skipped because moved file no longer exists");
                continue;
            }

            if (originalSource.getParent() != null && !Files.exists(originalSource.getParent())) {
                Files.createDirectories(originalSource.getParent());
            }

            if (Files.exists(originalSource)) {
                System.out.println("Skipping rollback because original path already exists: " + originalSource);
                logger.logSkipped(originalSource, "Rollback skipped because original destination already exists");
                continue;
            }

            Files.move(currentLocation, originalSource);
            rollbackCount++;

            System.out.println("Rolled back: " + currentLocation + " -> " + originalSource);
            logger.logRollback(currentLocation, originalSource);
        }

        System.out.println("Rollback complete. Restored " + rollbackCount + " file(s).");
    }

    private List<MoveRecord> readMoveRecords(Path logFilePath) throws IOException {
        List<MoveRecord> records = new ArrayList<>();

        if (!Files.exists(logFilePath)) {
            return records;
        }

        List<String> lines = Files.readAllLines(logFilePath);

        for (String line : lines) {
            if (!line.contains("| MOVED |")) {
                continue;
            }

            String[] parts = line.split("\\|");

            if (parts.length < 3) {
                continue;
            }

            String movePart = parts[2].trim();

            String[] pathParts = movePart.split(" -> ");

            if (pathParts.length != 2) {
                continue;
            }

            Path sourcePath = Path.of(pathParts[0].trim());
            Path destinationPath = Path.of(pathParts[1].trim());

            records.add(new MoveRecord(sourcePath, destinationPath));
        }

        return records;
    }
}