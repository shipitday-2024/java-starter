package io.github.adamw7.orchestrator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Getter
public class ClassContainer {
    private final Path originalPath;
    @With
    private final String className;
    private final String originalCode;

    @With
    private String generatedCode;

    @With
    private Path targetPath;

    @With
    private String lastError;

    // TODO: why do we need Set here?
    @With
    private Set<String> slowTests;

    @With
    private Map<Integer, List<String>> codeStyleIssues;

    @With
    private Path module;

    public ClassContainer(Path originalPath, String className, Path module) {
        this.originalPath = originalPath;
        this.className = className;
        this.module = module;
        originalCode = readCode();
    }

    public boolean hasError() {
        return lastError != null && !lastError.isBlank();
    }

    public boolean hasSlowTest() { return slowTests != null && !slowTests.isEmpty(); }

    public boolean hasCodeStyleIssues() {
        return codeStyleIssues != null && !codeStyleIssues.isEmpty();
    }

    public boolean hasModule() {
        return Optional.ofNullable(module)
                .map(Path::toString)
                .filter(s -> !s.isBlank())
                .isPresent();
    }

    private String readCode() {
        try {
            return Files.readString(originalPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
