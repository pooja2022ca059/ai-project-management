package com.aipm.ai_project_management.modules.tasks.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskCommentDTO {
    private Long id;
    private String content;
    private Long taskId;
    private UserSummaryDTO author;
    private List<UserSummaryDTO> mentions = new ArrayList<>();
    private Long parentId;
    private List<TaskCommentDTO> replies = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default constructor
    public TaskCommentDTO() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public UserSummaryDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserSummaryDTO author) {
        this.author = author;
    }

    public List<UserSummaryDTO> getMentions() {
        return mentions;
    }

    public void setMentions(List<UserSummaryDTO> mentions) {
        this.mentions = mentions;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<TaskCommentDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<TaskCommentDTO> replies) {
        this.replies = replies;
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
    }
}