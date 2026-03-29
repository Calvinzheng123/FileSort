package service;

import model.Rule;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {

    private final Properties properties = new Properties();

    public ConfigLoader(Path configPath) throws IOException {
        try (InputStream input = Files.newInputStream(configPath)) {
            properties.load(input);
        }
    }

    public Path getBaseFolder() {
        String folder = properties.getProperty("base.folder");
        return Path.of(folder);
    }

    public int getMoveLimit() {
        String value = properties.getProperty("move.limit", "-1").trim();
        return Integer.parseInt(value);
    }

    public List<Rule> loadRules(Path baseFolder) {
        List<Rule> rules = new ArrayList<>();

        int ruleCount = Integer.parseInt(properties.getProperty("rule.count", "0"));

        for (int i = 1; i <= ruleCount; i++) {
            String prefix = "rule." + i + ".";

            List<String> extensions = parseExtensions(properties.getProperty(prefix + "extensions", ""));
            String keyword = emptyToNull(properties.getProperty(prefix + "keyword", ""));
            Integer olderThanDays = parseInteger(properties.getProperty(prefix + "olderThanDays", ""));
            String destination = properties.getProperty(prefix + "destination", "").trim();

            Rule rule = new Rule(
                    extensions,
                    keyword,
                    olderThanDays,
                    baseFolder.resolve(destination)
            );

            rules.add(rule);
        }

        return rules;
    }

    private List<String> parseExtensions(String value) {
        value = value.trim();

        if (value.isEmpty()) {
            return null;
        }

        String[] parts = value.split(",");
        List<String> extensions = new ArrayList<>();

        for (String part : parts) {
            String cleaned = part.trim().toLowerCase();
            if (!cleaned.isEmpty()) {
                extensions.add(cleaned);
            }
        }

        return extensions.isEmpty() ? null : extensions;
    }

    private String emptyToNull(String value) {
        value = value.trim();
        return value.isEmpty() ? null : value;
    }

    private Integer parseInteger(String value) {
        value = value.trim();
        return value.isEmpty() ? null : Integer.parseInt(value);
    }
}