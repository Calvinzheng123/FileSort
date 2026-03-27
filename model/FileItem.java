package model;
import java.nio.file.Path;

public class FileItem {
    private Path path;
    private String fileName;
    private String extension;
    private long size;

    public FileItem(Path path, String fileName, String extension, long size) {
        this.path = path;
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
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
    
}
