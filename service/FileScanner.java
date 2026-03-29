package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import model.FileItem;

/*
Responsible for scanning a directory and converting files into FileItem objects.

This class interacts directly with the filesystem and extracts metadata for each file.
In its current configuration, it scans only the top-level directory to avoid
modifying files inside nested folders (e.g., project directories).

Key responsibilities:
- iterate through files in a directory
- extract file metadata (name, extension, size, last modified time)
- return a list of FileItem objects for further processing

design:
- separates filesystem access from business logic
- ensures safe operation by ignoring directories
*/

public class FileScanner {

    public List<FileItem> scanFolder(Path folderPath) throws IOException {
        List<FileItem> files = new ArrayList<>();

        try (var stream = Files.list(folderPath)) {
            for (Path path : stream.toList()) {
                if (Files.isRegularFile(path)) {
                    String fileName = path.getFileName().toString();
                    String extension = getExtension(fileName);
                    long size = Files.size(path);
                    FileTime lastModifiedTime = Files.getLastModifiedTime(path);

                    FileItem fileItem = new FileItem(path, fileName, extension, size, lastModifiedTime);
                    files.add(fileItem);
                }
            }
        }

        return files;
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}