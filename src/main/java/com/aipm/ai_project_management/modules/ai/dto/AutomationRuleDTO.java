package com.aipm.ai_project_management.modules.ai.dto;

import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AutomationRuleDTO {
    
    private Long id;
    
    @NotBlank(message = "Rule name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Rule type is required")
    private AutomationRuleEntity.RuleType type;
    
    private String triggerConditions;
    
    private String actions;
    
    private Boolean isActive;
    
    private Long ruleCreatedBy;
    
    private Long projectId;
    
    @NotNull(message = "Priority is required")
    private AutomationRuleEntity.RulePriority priority;
    
    private Long executionCount;
    
    private LocalDateTime lastExecutedAt;
    
    private Long successCount;
    
    private Long failureCount;
    
    private String lastError;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Calculated fields
    private Double successRate;
    
    private String performanceStatus;
    
    // Constructors
    public AutomationRuleDTO() {
        this.isActive = true;
        this.priority = AutomationRuleEntity.RulePriority.MEDIUM;
        this.executionCount = 0L;
        this.successCount = 0L;
        this.failureCount = 0L;
    }
    
    public AutomationRuleDTO(String name, AutomationRuleEntity.RuleType type, Long ruleCreatedBy) {
        this();
        this.name = name;
        this.type = type;
        this.ruleCreatedBy = ruleCreatedBy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public AutomationRuleEntity.RuleType getType() {
        return type;
    }
    
    public void setType(AutomationRuleEntity.RuleType type) {
        this.type = type;
    }
    
    public String getTriggerConditions() {
        return triggerConditions;
    }
    
    public void setTriggerConditions(String triggerConditions) {
        this.triggerConditions = triggerConditions;
    }
    
    public String getActions() {
        return actions;
    }
    
    public void setActions(String actions) {
        this.actions = actions;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Long getRuleCreatedBy() {
        return ruleCreatedBy;
    }
    
    public void setRuleCreatedBy(Long ruleCreatedBy) {
        this.ruleCreatedBy = ruleCreatedBy;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public AutomationRuleEntity.RulePriority getPriority() {
        return priority;
    }
    
    public void setPriority(AutomationRuleEntity.RulePriority priority) {
        this.priority = priority;
    }
    
    public Long getExecutionCount() {
        return executionCount;
    }
    
    public void setExecutionCount(Long executionCount) {
        this.executionCount = executionCount;
    }
    
    public LocalDateTime getLastExecutedAt() {
        return lastExecutedAt;
    }
    
    public void setLastExecutedAt(LocalDateTime lastExecutedAt) {
        this.lastExecutedAt = lastExecutedAt;
    }
    
    public Long getSuccessCount() {
        return successCount;
    }
    
    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }
    
    public Long getFailureCount() {
        return failureCount;
    }
    
    public void setFailureCount(Long failureCount) {
        this.failureCount = failureCount;
    }
    
    public String getLastError() {
        return lastError;
    }
    
    public void setLastError(String lastError) {
        this.lastError = lastError;
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
    
    public Double getSuccessRate() {
        if (executionCount == null || executionCount == 0) {
            return 0.0;
        }
        return (successCount != null ? successCount : 0L) * 100.0 / executionCount;
    }
    
    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }
    
    public String getPerformanceStatus() {
        Double rate = getSuccessRate();
        if (executionCount == null || executionCount < 5) {
            return "NOT_ENOUGH_DATA";
        } else if (rate >= 90) {
            return "EXCELLENT";
        } else if (rate >= 75) {
            return "GOOD";
        } else if (rate >= 50) {
            return "FAIR";
        } else {
            return "POOR";
        }
    }
    
    public void setPerformanceStatus(String performanceStatus) {
        this.performanceStatus = performanceStatus;
    }
}