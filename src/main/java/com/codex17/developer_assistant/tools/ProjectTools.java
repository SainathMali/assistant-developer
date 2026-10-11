package com.codex17.developer_assistant.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectTools {

    private static final String PROJECT_ROOT = "src/main/java";

    @Tool(description = "list all the Java source files in the project")
    public List<String> listProjectFiles() {



    }

    @Tool(description = "Searches for a keyword across the codebase")
    public List<String> searchCodebase(String keyword) {

    }
}