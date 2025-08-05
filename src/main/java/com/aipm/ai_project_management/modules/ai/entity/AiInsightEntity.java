package com.aipm.ai_project_management.modules.ai.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_insights")
public class AiInsightEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightType type;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "insight_data", columnDefinition = "TEXT")
    private String insightData; // JSON data for complex insights

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightPriority priority = InsightPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightStatus status = InsightStatus.ACTIVE;

    @Column(name = "confidence_score")
    private Double confidenceScore; // 0.0 to 1.0

    @Column(name = "related_entity_type")
    private String relatedEntityType; // e.g., "PROJECT", "TASK", "TEAM"

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Column(name = "generated_by")
    private String generatedBy; // AI model/algorithm name

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "action_taken")
    private Boolean actionTaken = false;

    @Column(name = "action_taken_at")
    private LocalDateTime actionTakenAt;

    @Column(name = "action_taken_by")
    private Long actionTakenBy;

    // Constructors
    public AiInsightEntity() {
    }

    public AiInsightEntity(InsightType type, String title, String description) {
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

    public InsightType getType() {
        return type;
    }

    public void setType(InsightType type) {
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

    public String getInsightData() {
        return insightData;
    }

    public void setInsightData(String insightData) {
        this.insightData = insightData;
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

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(Boolean actionTaken) {
        this.actionTaken = actionTaken;
    }

    public LocalDateTime getActionTakenAt() {
        return actionTakenAt;
    }

    public void setActionTakenAt(LocalDateTime actionTakenAt) {
        this.actionTakenAt = actionTakenAt;
    }

    public Long getActionTakenBy() {
        return actionTakenBy;
    }

    public void setActionTakenBy(Long actionTakenBy) {
        this.actionTakenBy = actionTakenBy;
    }

    // Enums
    public enum InsightType {
        PROJECT_RISK_ALERT,
        DEADLINE_PREDICTION,
        RESOURCE_OPTIMIZATION,
        PERFORMANCE_ANALYSIS,
        BUDGET_FORECAST,
        TASK_RECOMMENDATION,
        TEAM_PRODUCTIVITY,
        SKILL_GAP_ANALYSIS,
        WORKLOAD_BALANCE,
        QUALITY_PREDICTION
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
        EXPIRED,
        IMPLEMENTED
    }
}