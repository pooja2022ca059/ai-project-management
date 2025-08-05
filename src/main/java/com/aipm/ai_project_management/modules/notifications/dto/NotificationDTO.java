package com.aipm.ai_project_management.modules.notifications.dto;

import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class NotificationDTO {
    
    private Long id;
    
    @NotNull(message = "Recipient ID is required")
    private Long recipientId;
    
    private Long senderId;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String message;
    
    @NotNull(message = "Notification type is required")
    private NotificationEntity.NotificationType type;
    
    @NotNull(message = "Priority is required")
    private NotificationEntity.NotificationPriority priority;
    
    private Boolean isRead;
    private LocalDateTime readAt;
    private String relatedEntityType;
    private Long relatedEntityId;
    private String actionUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public NotificationDTO() {
    }
    
    public NotificationDTO(Long recipientId, String title, String message, NotificationEntity.NotificationType type) {
        this.recipientId = recipientId;
        this.title = title;
        this.message = message;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRecipientId() {
        return recipientId;
    }
    
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
    
    public Long getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public NotificationEntity.NotificationType getType() {
        return type;
    }
    
    public void setType(NotificationEntity.NotificationType type) {
        this.type = type;
    }
    
    public NotificationEntity.NotificationPriority getPriority() {
        return priority;
    }
    
    public void setPriority(NotificationEntity.NotificationPriority priority) {
        this.priority = priority;
    }
    
    public Boolean getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    
    public LocalDateTime getReadAt() {
        return readAt;
    }
    
    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
    
    public String getRelatedEntityType() {
        return relatedEntityType;
    }
    
    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }
    
    public Long getRelatedEntityId() {
        return relatedEntityId;
    }
    
    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }
    
    public String getActionUrl() {
        return actionUrl;
    }
    
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
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