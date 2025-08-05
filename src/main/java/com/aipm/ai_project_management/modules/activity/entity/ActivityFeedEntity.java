package com.aipm.ai_project_management.modules.activity.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_feed")
public class ActivityFeedEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "actor_id", nullable = false)
    private Long actorId; // User who performed the action

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "entity_type")
    private String entityType; // PROJECT, TASK, USER, etc.

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON for additional data

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    // Constructors
    public ActivityFeedEntity() {
    }

    public ActivityFeedEntity(Long userId, Long actorId, ActivityType type, String title, String description) {
        this.userId = userId;
        this.actorId = actorId;
        this.type = type;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
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

    // Enums
    public enum ActivityType {
        PROJECT_CREATED,
        PROJECT_UPDATED,
        PROJECT_COMPLETED,
        TASK_CREATED,
        TASK_ASSIGNED,
        TASK_UPDATED,
        TASK_COMPLETED,
        COMMENT_ADDED,
        FILE_UPLOADED,
        TEAM_MEMBER_ADDED,
        TEAM_MEMBER_REMOVED,
        DEADLINE_CHANGED,
        STATUS_CHANGED,
        USER_MENTIONED,
        MILESTONE_REACHED,
        BUDGET_UPDATED,
        TIME_LOGGED,
        APPROVAL_REQUESTED,
        APPROVAL_GRANTED,
        APPROVAL_REJECTED
    }
}