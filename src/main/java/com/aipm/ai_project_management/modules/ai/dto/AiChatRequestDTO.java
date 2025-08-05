package com.aipm.ai_project_management.modules.ai.dto;

import jakarta.validation.constraints.NotBlank;

public class AiChatRequestDTO {
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private String context; // Optional context about current project/task
    
    private Long projectId;
    
    private Long taskId;
    
    private String conversationId; // To maintain chat sessions
    
    private String chatType; // GENERAL, PROJECT_HELP, TASK_ANALYSIS, etc.
    
    // Constructors
    public AiChatRequestDTO() {
    }
    
    public AiChatRequestDTO(String message) {
        this.message = message;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getContext() {
        return context;
    }
    
    public void setContext(String context) {
        this.context = context;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getChatType() {
        return chatType;
    }
    
    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}