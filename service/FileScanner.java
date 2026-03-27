package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import model.FileItem;

/*
Responsible for scanning directories and converting real files into FileItem objects.

Key behavior:
- Recursively traverses the directory tree using Files.walk()
- Filters out directories (only processes regular files)
- Extracts file metadata (name, extension, size, last modified time)

Output:
- Returns a list of FileItem objects representing all discovered files

notes:
- Acts as the bridge between filesystem and application logic
- Keeps scanning logic separate from classification and movement
*/

public class FileScanner {

    public List<FileItem> scanFolder(Path folderPath) throws IOException {
        List<FileItem> files = new ArrayList<>();

        try (var stream = Files.walk(folderPath)) {
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