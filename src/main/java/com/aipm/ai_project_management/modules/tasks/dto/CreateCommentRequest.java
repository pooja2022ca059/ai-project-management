package com.aipm.ai_project_management.modules.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class CreateCommentRequest {
    
    private Long taskId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Content must be less than 2000 characters")
    private String content;
    
    private List<Long> mentions = new ArrayList<>();
    
    private Long parentId;
    
    // Default constructor
    public CreateCommentRequest() {
    }
    
    // Constructor with fields
    public CreateCommentRequest(Long taskId, Long userId, String content, List<Long> mentions, Long parentId) {
        this.taskId = taskId;
        this.userId = userId;
        this.content = content;
        this.mentions = mentions;
        this.parentId = parentId;
    }
    
    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public List<Long> getMentions() {
        return mentions;
    }
    
    public void setMentions(List<Long> mentions) {
        this.mentions = mentions;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
