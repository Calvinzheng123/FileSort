package service;

import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

        boolean ageMatches = rule.getOlderThanDays() == null ||
                isOlderThan(file, rule.getOlderThanDays());

        return extensionMatches && keywordMatches && ageMatches;
    }

    private boolean isOlderThan(FileItem file, int days) {
        Instant cutoff = Instant.now().minus(days, ChronoUnit.DAYS);
        Instant fileModified = file.getLastModifiedTime().toInstant();
        return fileModified.isBefore(cutoff);
    }
}