package com.codex17.developer_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeveloperAssistantAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeveloperAssistantAiApplication.class, args);

        System.out.println(
                "API key loaded: " +
                        (System.getenv("OPENROUTER_API_KEY") != null &&
                                !System.getenv("OPENROUTER_API_KEY").isBlank())
        );
	}

}
