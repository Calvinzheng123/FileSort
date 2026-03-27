package model;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/*
Defines a rule used to classify files.

A rule can match files based on:
- file extensions (e.g. jpg, png)
- keyword in filename (e.g. "resume", "screenshot")
- file age (older than X days)

Each rule maps matching files to a destination folder.

Important behavior:
- Any field can be null, meaning "no restriction" for that condition
- Rules are evaluated in order (priority matters)

Examples:
- Match all files containing "resume"
- Match image files with "screenshot" in the name
- Match files older than 2 years

Design notes:
- Flexible rule definition supports combining conditions
- Enables extensible rule system without hardcoding logic
*/

public class Rule {
    private final List<String> extensions;
    private final String keyword;
    private final Integer olderThanDays;
    private final Path destinationFolder;

    public Rule(List<String> extensions, String keyword, Integer olderThanDays, Path destinationFolder) {
        this.extensions = extensions == null ? null :
                extensions.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());

        this.keyword = keyword == null ? null : keyword.toLowerCase();
        this.olderThanDays = olderThanDays;
        this.destinationFolder = destinationFolder;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public String getKeyword() {
        return keyword;
    }

    public Integer getOlderThanDays() {
        return olderThanDays;
    }

    public Path getDestinationFolder() {
        return destinationFolder;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "extensions=" + extensions +
                ", keyword='" + keyword + '\'' +
                ", olderThanDays=" + olderThanDays +
                ", destinationFolder=" + destinationFolder +
                '}';
    }
}