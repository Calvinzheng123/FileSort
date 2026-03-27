package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {

    private final Path logFilePath;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AppLogger(Path logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void logMove(Path source, Path destination) throws IOException {
        String timestamp = LocalDateTime.now().format(formatter);
        String message = timestamp + " | MOVED | " + source + " -> " + destination + System.lineSeparator();

        writeLog(message);
    }

    public void logSkipped(Path file, String reason) throws IOException {
        String timestamp = LocalDateTime.now().format(formatter);
        String message = timestamp + " | SKIPPED | " + file + " | " + reason + System.lineSeparator();

        writeLog(message);
    }

    public void logError(Path file, String reason) throws IOException {
        String timestamp = LocalDateTime.now().format(formatter);
        String message = timestamp + " | ERROR | " + file + " | " + reason + System.lineSeparator();

        writeLog(message);
    }

    private void writeLog(String message) throws IOException {
        if (logFilePath.getParent() != null && !Files.exists(logFilePath.getParent())) {
            Files.createDirectories(logFilePath.getParent());
        }

        Files.writeString(
                logFilePath,
                message,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}