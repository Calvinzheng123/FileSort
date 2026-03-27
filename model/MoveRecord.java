package model;

import java.nio.file.Path;

public class MoveRecord {
    private final Path sourcePath;
    private final Path destinationPath;

    public MoveRecord(Path sourcePath, Path destinationPath) {
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getDestinationPath() {
        return destinationPath;
    }

    @Override
    public String toString() {
        return "MoveRecord{" +
                "sourcePath=" + sourcePath +
                ", destinationPath=" + destinationPath +
                '}';
    }
}