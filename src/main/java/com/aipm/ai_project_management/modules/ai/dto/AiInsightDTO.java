package com.aipm.ai_project_management.modules.ai.dto;

import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AiInsightDTO {
    
    private Long id;
    
    @NotNull(message = "Insight type is required")
    private AiInsightEntity.InsightType type;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String insightData;
    
    @NotNull(message = "Priority is required")
    private AiInsightEntity.InsightPriority priority;
    
    private AiInsightEntity.InsightStatus status;
    
    private Double confidenceScore;
    
    private String relatedEntityType;
    
    private Long relatedEntityId;
    
    private String generatedBy;
    
    private LocalDateTime expiresAt;
    
    private Boolean actionTaken;
    
    private LocalDateTime actionTakenAt;
    
    private Long actionTakenBy;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public AiInsightDTO() {
    }
    
    public AiInsightDTO(AiInsightEntity.InsightType type, String title, String description) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.priority = AiInsightEntity.InsightPriority.MEDIUM;
        this.status = AiInsightEntity.InsightStatus.ACTIVE;
        this.actionTaken = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public AiInsightEntity.InsightType getType() {
        return type;
    }
    
    public void setType(AiInsightEntity.InsightType type) {
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
    
    public AiInsightEntity.InsightPriority getPriority() {
        return priority;
    }
    
    public void setPriority(AiInsightEntity.InsightPriority priority) {
        this.priority = priority;
    }
    
    public AiInsightEntity.InsightStatus getStatus() {
        return status;
    }
    
    public void setStatus(AiInsightEntity.InsightStatus status) {
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