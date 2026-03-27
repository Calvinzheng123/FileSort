package model;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/*
Represents a single file discovered during scanning.

This is an immutable data object that stores all metadata needed for
classification and movement:
- full file path
- file name
- extension
- file size
- last modified time

FileItem acts as the core unit passed between components:
Scanner → RuleEngine → FileMover

notes:
- Immutable (all fields final) to prevent accidental mutation
- Contains only data, no logic (separation of concerns)
*/

public class FileItem {

    private final Path path;
    private final String fileName;
    private final String extension;
    private final long size;
    private final FileTime lastModifiedTime;

    public FileItem(Path path, String fileName, String extension, long size, FileTime lastModifiedTime) {
        this.path = path;
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
        this.lastModifiedTime = lastModifiedTime;
    }

    public Path getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExtension() {
        return extension;
    }

    public long getSize() {
        return size;
    }

    public FileTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "path=" + path +
                ", fileName='" + fileName + '\'' +
                ", extension='" + extension + '\'' +
                ", size=" + size +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}