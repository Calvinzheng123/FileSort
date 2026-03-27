package service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.FileItem;
import model.Rule;

public class RuleEngine {

    public Map<FileItem, Path> generatePreview(List<FileItem> files, List<Rule> rules) {
        Map<FileItem, Path> preview = new HashMap<>();

        for (FileItem file : files) {
            Path destination = findDestination(file, rules);
            preview.put(file, destination);
        }

        return preview;
    }

    private Path findDestination(FileItem file, List<Rule> rules) {
        for (Rule rule : rules) {
            if (matchesRule(file, rule)) {
                return rule.getDestinationFolder();
            }
        }

        return null;
    }

    private boolean matchesRule(FileItem file, Rule rule) {
        boolean extensionMatches = rule.getExtensions() == null ||
                rule.getExtensions().contains(file.getExtension());

        boolean keywordMatches = rule.getKeyword() == null ||
                file.getFileName().toLowerCase().contains(rule.getKeyword());

        return extensionMatches && keywordMatches;
    }
}