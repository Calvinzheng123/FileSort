package service;

import model.FileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMover {

    public void moveFile(FileItem file, Path destinationFolder) throws IOException {

        if (!Files.exists(destinationFolder)) {
            Files.createDirectories(destinationFolder);
        }

        Path targetPath = destinationFolder.resolve(file.getFileName());
        Path safeTargetPath = getSafeTargetPath(targetPath);

        Files.move(file.getPath(), safeTargetPath);

        System.out.println("Moved: " + file.getFileName() + " -> " + safeTargetPath);
    }

    private Path getSafeTargetPath(Path targetPath) {
        if (!Files.exists(targetPath)) {
            return targetPath;
        }

        String fileName = targetPath.getFileName().toString();
        String baseName = getBaseName(fileName);
        String extension = getExtension(fileName);

        int counter = 1;

        while (true) {
            String newFileName;

            if (extension.isEmpty()) {
                newFileName = baseName + "(" + counter + ")";
            } else {
                newFileName = baseName + "(" + counter + ")." + extension;
            }

            Path newTargetPath = targetPath.getParent().resolve(newFileName);

            if (!Files.exists(newTargetPath)) {
                return newTargetPath;
            }

            counter++;
        }
    }

    private String getBaseName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1) {
            return fileName;
        }

        return fileName.substring(0, lastDotIndex);
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1);
    }
}