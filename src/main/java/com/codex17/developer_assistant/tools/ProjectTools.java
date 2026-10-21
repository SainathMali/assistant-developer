
package com.codex17.developer_assistant.tools;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ProjectTools {

    private static final Path PROJECT_ROOT =
            Paths.get("").toAbsolutePath().normalize();

    @Tool(description = "List all Java source files in the project")
    public List<String> listProjectFiles() {
        Path javaRoot = PROJECT_ROOT.resolve("src/main/java");

        try (Stream<Path> paths = Files.walk(javaRoot)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(PROJECT_ROOT::relativize)
                    .map(Path::toString)
                    .toList();

        } catch (IOException e) {
            return List.of("Error listing project files: " + e.getMessage());
        }
    }

    @Tool(description =
            "Search Java source files for a keyword and return matching file paths")
    public List<String> searchCodebase(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of("Please provide a keyword to search.");
        }

        Path javaRoot = PROJECT_ROOT.resolve("src/main/java");

        try (Stream<Path> paths = Files.walk(javaRoot)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> containsKeyword(path, keyword))
                    .map(PROJECT_ROOT::relativize)
                    .map(Path::toString)
                    .toList();

        } catch (IOException e) {
            return List.of("Error searching codebase: " + e.getMessage());
        }
    }

    @Tool(description =
            "Read the contents of a project source file using its relative path, "
                    + "for example src/main/java/com/example/MyClass.java")
    public String readProjectFile(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return "Please provide a file path.";
        }

        try {
            Path file = PROJECT_ROOT.resolve(relativePath)
                    .normalize()
                    .toAbsolutePath();

            if (!file.startsWith(PROJECT_ROOT)) {
                return "Access denied: path is outside the project directory.";
            }

            if (!Files.isRegularFile(file)) {
                return "File not found: " + relativePath;
            }

            if (!file.toString().endsWith(".java")
                    && !file.getFileName().toString().equals("pom.xml")) {
                return "Access denied: only Java source files and pom.xml are allowed.";
            }

            if (Files.size(file) > 200_000) {
                return "File is too large to read.";
            }

            return Files.readString(file);

        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    private boolean containsKeyword(Path path, String keyword) {
        try {
            String content = Files.readString(path);
            return content.toLowerCase()
                    .contains(keyword.toLowerCase());
        } catch (IOException e) {
            return false;
        }
    }
}
