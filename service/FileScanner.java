package service;

import model.FileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    public List<FileItem> scanFolder(Path folderPath) throws IOException {
        List<FileItem> files = new ArrayList<>();

        try (var stream = Files.walk(folderPath)) {
            for (Path path : stream.toList()) {
                if (Files.isRegularFile(path)) {
                    String fileName = path.getFileName().toString();
                    String extension = getExtension(fileName);
                    long size = Files.size(path);

                    FileItem fileItem = new FileItem(path, fileName, extension, size);
                    files.add(fileItem);
                }
            }
        }
        return files;
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return ""; // no extension or empty extension
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
    
}
