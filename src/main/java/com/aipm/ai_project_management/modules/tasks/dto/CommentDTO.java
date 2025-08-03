package com.aipm.ai_project_management.modules.tasks.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    
    private Long id;
    private Long taskId;
    private Long userId;
    private String userFullName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean edited;

    // Default constructor
    public CommentDTO() {
    }

    // Constructor with basic fields
    public CommentDTO(Long id, Long taskId, Long userId, String content) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.edited = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        this.edited = true;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", userId=" + userId +
                ", userFullName='" + userFullName + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", edited=" + edited +
                '}';
    }
}
