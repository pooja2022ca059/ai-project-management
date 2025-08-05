package com.aipm.ai_project_management.modules.shared.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing AI-generated insights and suggestions.
 * Stores AI analysis results for projects, tasks, and team performance.
 */
@Entity
@Table(name = "ai_insights")
public class AiInsightEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightType type;

    @Column(name = "entity_type", nullable = false)
    private String entityType; // e.g., "PROJECT", "TASK", "TEAM", "USER"

    @Column(name = "entity_id", nullable = false)
    private Long entityId; // ID of the related entity

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightPriority priority = InsightPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightStatus status = InsightStatus.ACTIVE;

    @Column(name = "confidence_score")
    private Double confidenceScore; // AI confidence level (0.0 to 1.0)

    @Column(name = "impact_score")
    private Double impactScore; // Predicted impact level (0.0 to 1.0)

    @Column(name = "suggested_actions", columnDefinition = "JSON")
    private String suggestedActions; // JSON array of suggested actions

    @Column(name = "data_points", columnDefinition = "JSON")
    private String dataPoints; // JSON object with supporting data

    @Column(name = "ai_model_version")
    private String aiModelVersion; // Version of AI model that generated this insight

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // When this insight becomes irrelevant

    @Column(name = "viewed_by_user_ids", columnDefinition = "JSON")
    private String viewedByUserIds; // JSON array of user IDs who have seen this insight

    @Column(name = "is_acted_upon")
    private Boolean isActedUpon = false;

    @Column(name = "acted_upon_at")
    private LocalDateTime actedUponAt;

    @Column(name = "feedback_rating")
    private Integer feedbackRating; // User feedback on insight usefulness (1-5)

    @Column(name = "feedback_comments")
    private String feedbackComments;

    // Default constructor
    public AiInsightEntity() {
        this.generatedAt = LocalDateTime.now();
    }

    // Constructor with basic fields
    public AiInsightEntity(InsightType type, String entityType, Long entityId, String title, String description) {
        this();
        this.type = type;
        this.entityType = entityType;
        this.entityId = entityId;
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

    public InsightType getType() {
        return type;
    }

    public void setType(InsightType type) {
        this.type = type;
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

    public InsightPriority getPriority() {
        return priority;
    }

    public void setPriority(InsightPriority priority) {
        this.priority = priority;
    }

    public InsightStatus getStatus() {
        return status;
    }

    public void setStatus(InsightStatus status) {
        this.status = status;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public Double getImpactScore() {
        return impactScore;
    }

    public void setImpactScore(Double impactScore) {
        this.impactScore = impactScore;
    }

    public String getSuggestedActions() {
        return suggestedActions;
    }

    public void setSuggestedActions(String suggestedActions) {
        this.suggestedActions = suggestedActions;
    }

    public String getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(String dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getAiModelVersion() {
        return aiModelVersion;
    }

    public void setAiModelVersion(String aiModelVersion) {
        this.aiModelVersion = aiModelVersion;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getViewedByUserIds() {
        return viewedByUserIds;
    }

    public void setViewedByUserIds(String viewedByUserIds) {
        this.viewedByUserIds = viewedByUserIds;
    }

    public Boolean getIsActedUpon() {
        return isActedUpon;
    }

    public void setIsActedUpon(Boolean isActedUpon) {
        this.isActedUpon = isActedUpon;
        if (isActedUpon && actedUponAt == null) {
            this.actedUponAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getActedUponAt() {
        return actedUponAt;
    }

    public void setActedUponAt(LocalDateTime actedUponAt) {
        this.actedUponAt = actedUponAt;
    }

    public Integer getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(Integer feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackComments() {
        return feedbackComments;
    }

    public void setFeedbackComments(String feedbackComments) {
        this.feedbackComments = feedbackComments;
    }

    // Enums
    public enum InsightType {
        PERFORMANCE_OPTIMIZATION,
        RISK_DETECTION,
        DEADLINE_PREDICTION,
        RESOURCE_OPTIMIZATION,
        QUALITY_IMPROVEMENT,
        COST_OPTIMIZATION,
        WORKFLOW_SUGGESTION,
        TREND_ANALYSIS,
        ANOMALY_DETECTION,
        RECOMMENDATION
    }

    public enum InsightPriority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum InsightStatus {
        ACTIVE,
        DISMISSED,
        ACTED_UPON,
        EXPIRED
    }
}