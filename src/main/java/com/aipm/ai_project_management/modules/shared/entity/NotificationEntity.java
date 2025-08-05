package com.aipm.ai_project_management.modules.shared.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing notifications in the system.
 * Supports real-time notifications for various user activities.
 */
@Entity
@Table(name = "notifications")
public class NotificationEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationPriority priority = NotificationPriority.NORMAL;

    @Column(name = "entity_type")
    private String entityType; // e.g., "TASK", "PROJECT", "USER"

    @Column(name = "entity_id")
    private Long entityId; // ID of the related entity

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "action_url")
    private String actionUrl; // URL to navigate to when notification is clicked

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata; // JSON string for additional data

    // Default constructor
    public NotificationEntity() {
    }

    // Constructor with basic fields
    public NotificationEntity(Long recipientId, String title, String message, NotificationType type) {
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

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
        if (isRead && readAt == null) {
            this.readAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    // Enums
    public enum NotificationType {
        TASK_ASSIGNED,
        TASK_COMPLETED,
        TASK_OVERDUE,
        PROJECT_CREATED,
        PROJECT_UPDATED,
        DEADLINE_APPROACHING,
        COMMENT_ADDED,
        MENTION,
        SYSTEM_ALERT,
        AI_INSIGHT
    }

    public enum NotificationPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }
}