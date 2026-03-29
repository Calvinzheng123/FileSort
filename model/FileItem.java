package model;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

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