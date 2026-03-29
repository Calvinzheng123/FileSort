package model;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/*
This defines a rule used to classify files and determine their destination folder.

A rule can match files based on three optional conditions:
- file extension(s)
- keyword in filename
- file age (older than a specified number of days)

If a condition is null, it is ignored.

Each rule maps matching files to a destination directory.

design:
- flexible rule structure which allows combining multiple conditions
- supports priority-based evaluation (order matters in RuleEngine)
- enables external configuration via config file
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