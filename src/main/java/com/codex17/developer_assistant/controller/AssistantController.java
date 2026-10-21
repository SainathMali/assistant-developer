package com.codex17.developer_assistant.controller;

import com.codex17.developer_assistant.tools.ProjectTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assistant")
public class AssistantController {

    private final ChatClient chatClient;

    public AssistantController(ChatClient.Builder builder, ProjectTools projectTools, ChatMemory chatMemory)
    {
        this.chatClient = builder
                .defaultSystem("""
            You are a Developer Assistant for this project.

            Rules:
            1. Answer using actual project files and conversation history.
            2. Never invent class names, methods, file paths, or dependencies.
            3. If you have not read a file, do not claim that you have.
            4. If information is missing, clearly say "I don't know".
            5. Distinguish verified facts from assumptions.
            6. Use project tools when questions require source code.
            7. Conversation memory is context, not proof of project facts.
            """)
                .defaultTools(projectTools)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message ,
                       @RequestParam (defaultValue = "Default-session")
                       String conversationId )
    {
        return chatClient.prompt()
                .user(message)
                .advisors(a ->a.param(ChatMemory.CONVERSATION_ID,conversationId))
                .call()
                .content();
    }

}
