package com.aipm.ai_project_management.modules.ai.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "automation_rules")
public class AutomationRuleEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleType type;

    @Column(name = "trigger_conditions", columnDefinition = "TEXT")
    private String triggerConditions; // JSON defining when rule should trigger

    @Column(name = "actions", columnDefinition = "TEXT")
    private String actions; // JSON defining what actions to take

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "rule_created_by", nullable = false)
    private Long ruleCreatedBy;

    @Column(name = "project_id")
    private Long projectId; // null for global rules

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RulePriority priority = RulePriority.MEDIUM;

    @Column(name = "execution_count")
    private Long executionCount = 0L;

    @Column(name = "last_executed_at")
    private LocalDateTime lastExecutedAt;

    @Column(name = "success_count")
    private Long successCount = 0L;

    @Column(name = "failure_count")
    private Long failureCount = 0L;

    @Column(name = "last_error")
    private String lastError;

    // Constructors
    public AutomationRuleEntity() {
    }

    public AutomationRuleEntity(String name, RuleType type, Long ruleCreatedBy) {
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

    public RuleType getType() {
        return type;
    }

    public void setType(RuleType type) {
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

    public RulePriority getPriority() {
        return priority;
    }

    public void setPriority(RulePriority priority) {
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

    // Helper methods
    public void incrementExecutionCount() {
        this.executionCount++;
        this.lastExecutedAt = LocalDateTime.now();
    }

    public void recordSuccess() {
        this.successCount++;
        this.lastError = null;
    }

    public void recordFailure(String error) {
        this.failureCount++;
        this.lastError = error;
    }

    // Enums
    public enum RuleType {
        TASK_AUTO_ASSIGNMENT,
        STATUS_CHANGE_TRIGGER,
        DEADLINE_NOTIFICATION,
        BUDGET_ALERT,
        RESOURCE_ALLOCATION,
        PROGRESS_UPDATE,
        RISK_ASSESSMENT,
        WORKLOAD_BALANCING,
        QUALITY_CHECK,
        COMMUNICATION_TRIGGER
    }

    public enum RulePriority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}