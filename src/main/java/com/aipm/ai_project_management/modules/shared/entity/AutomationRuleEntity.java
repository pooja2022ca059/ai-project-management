package com.aipm.ai_project_management.modules.shared.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing automation rules for AI-driven project management.
 * Defines triggers and actions for automated workflows.
 */
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TriggerEvent triggerEvent;

    @Column(name = "trigger_conditions", columnDefinition = "JSON", nullable = false)
    private String triggerConditions; // JSON object defining when rule should fire

    @Column(name = "actions", columnDefinition = "JSON", nullable = false)
    private String actions; // JSON array of actions to execute

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleScope scope = RuleScope.PROJECT;

    @Column(name = "scope_entity_id")
    private Long scopeEntityId; // ID of project, team, or user this rule applies to

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_by_id", nullable = false)
    private Long createdById;

    @Column(name = "execution_count")
    private Integer executionCount = 0;

    @Column(name = "last_executed_at")
    private LocalDateTime lastExecutedAt;

    @Column(name = "success_count")
    private Integer successCount = 0;

    @Column(name = "failure_count")
    private Integer failureCount = 0;

    @Column(name = "priority")
    private Integer priority = 50; // Rule execution priority (1-100)

    @Column(name = "cooldown_minutes")
    private Integer cooldownMinutes = 0; // Minimum minutes between executions

    @Column(name = "max_executions_per_day")
    private Integer maxExecutionsPerDay = 1000; // Daily execution limit

    @Column(name = "execution_window_start")
    private String executionWindowStart; // e.g., "09:00" - when rule can start executing

    @Column(name = "execution_window_end")
    private String executionWindowEnd; // e.g., "17:00" - when rule should stop executing

    @Column(name = "days_of_week")
    private String daysOfWeek; // e.g., "MON,TUE,WED,THU,FRI" - when rule can execute

    @Column(name = "ai_model_version")
    private String aiModelVersion; // Version of AI model used for this rule

    @Column(name = "configuration", columnDefinition = "JSON")
    private String configuration; // Additional rule configuration

    // Default constructor
    public AutomationRuleEntity() {
    }

    // Constructor with basic fields
    public AutomationRuleEntity(String name, RuleType type, TriggerEvent triggerEvent, Long createdById) {
        this.name = name;
        this.type = type;
        this.triggerEvent = triggerEvent;
        this.createdById = createdById;
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

    public TriggerEvent getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(TriggerEvent triggerEvent) {
        this.triggerEvent = triggerEvent;
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

    public RuleScope getScope() {
        return scope;
    }

    public void setScope(RuleScope scope) {
        this.scope = scope;
    }

    public Long getScopeEntityId() {
        return scopeEntityId;
    }

    public void setScopeEntityId(Long scopeEntityId) {
        this.scopeEntityId = scopeEntityId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Integer getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    public LocalDateTime getLastExecutedAt() {
        return lastExecutedAt;
    }

    public void setLastExecutedAt(LocalDateTime lastExecutedAt) {
        this.lastExecutedAt = lastExecutedAt;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getCooldownMinutes() {
        return cooldownMinutes;
    }

    public void setCooldownMinutes(Integer cooldownMinutes) {
        this.cooldownMinutes = cooldownMinutes;
    }

    public Integer getMaxExecutionsPerDay() {
        return maxExecutionsPerDay;
    }

    public void setMaxExecutionsPerDay(Integer maxExecutionsPerDay) {
        this.maxExecutionsPerDay = maxExecutionsPerDay;
    }

    public String getExecutionWindowStart() {
        return executionWindowStart;
    }

    public void setExecutionWindowStart(String executionWindowStart) {
        this.executionWindowStart = executionWindowStart;
    }

    public String getExecutionWindowEnd() {
        return executionWindowEnd;
    }

    public void setExecutionWindowEnd(String executionWindowEnd) {
        this.executionWindowEnd = executionWindowEnd;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getAiModelVersion() {
        return aiModelVersion;
    }

    public void setAiModelVersion(String aiModelVersion) {
        this.aiModelVersion = aiModelVersion;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    // Helper methods
    public void incrementExecutionCount() {
        this.executionCount = (this.executionCount == null ? 0 : this.executionCount) + 1;
        this.lastExecutedAt = LocalDateTime.now();
    }

    public void incrementSuccessCount() {
        this.successCount = (this.successCount == null ? 0 : this.successCount) + 1;
    }

    public void incrementFailureCount() {
        this.failureCount = (this.failureCount == null ? 0 : this.failureCount) + 1;
    }

    // Enums
    public enum RuleType {
        TASK_AUTOMATION,
        NOTIFICATION_AUTOMATION,
        ASSIGNMENT_AUTOMATION,
        STATUS_AUTOMATION,
        DEADLINE_AUTOMATION,
        RESOURCE_AUTOMATION,
        WORKFLOW_AUTOMATION,
        REPORTING_AUTOMATION,
        AI_SUGGESTION
    }

    public enum TriggerEvent {
        TASK_CREATED,
        TASK_UPDATED,
        TASK_COMPLETED,
        TASK_OVERDUE,
        PROJECT_CREATED,
        PROJECT_UPDATED,
        USER_ASSIGNED,
        DEADLINE_APPROACHING,
        SCHEDULE_BASED,
        METRIC_THRESHOLD,
        AI_PREDICTION,
        MANUAL_TRIGGER
    }

    public enum RuleScope {
        GLOBAL,        // Applies to entire system
        ORGANIZATION,  // Applies to organization
        PROJECT,       // Applies to specific project
        TEAM,          // Applies to specific team
        USER           // Applies to specific user
    }
}