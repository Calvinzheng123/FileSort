package model;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Rule {
    private final List<String> extensions;
    private final String keyword;
    private final Path destinationFolder;

    public Rule(List<String> extensions, String keyword, Path destinationFolder) {
        this.extensions = extensions == null ? null : 
        extensions.stream()
        .map(String::toLowerCase)
        .collect(Collectors.toList());
        this.keyword = keyword == null ? null : keyword.toLowerCase();
        this.destinationFolder = destinationFolder;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public String getKeyword() {
        return keyword;
    }

    public Path getDestinationFolder() {
        return destinationFolder;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "extensions='" + extensions + '\'' +
                ", keyword='" + keyword + '\'' +
                ", destinationFolder=" + destinationFolder +
                '}';
    }
}