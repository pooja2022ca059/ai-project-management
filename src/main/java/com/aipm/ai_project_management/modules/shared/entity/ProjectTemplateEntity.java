package com.aipm.ai_project_management.modules.shared.entity;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing project templates for easy project creation.
 * Templates can include predefined tasks, milestones, and configurations.
 */
@Entity
@Table(name = "project_templates")
public class ProjectTemplateEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "category")
    private String category; // e.g., "Software Development", "Marketing", "Design"

    @Enumerated(EnumType.STRING)
    @Column(name = "default_priority")
    private ProjectPriority defaultPriority = ProjectPriority.MEDIUM;

    @Column(name = "estimated_duration_days")
    private Integer estimatedDurationDays;

    @Column(name = "estimated_hours")
    private Double estimatedHours;

    @Column(name = "default_budget")
    private Double defaultBudget;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false; // Whether template is available to all users

    @Column(name = "created_by_id", nullable = false)
    private Long createdById;

    @Column(name = "usage_count")
    private Integer usageCount = 0; // How many times this template has been used

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Template configuration as JSON
    @Column(name = "task_templates", columnDefinition = "JSON")
    private String taskTemplates; // JSON array of predefined tasks

    @Column(name = "milestone_templates", columnDefinition = "JSON")
    private String milestoneTemplates; // JSON array of predefined milestones

    @Column(name = "team_roles", columnDefinition = "JSON")
    private String teamRoles; // JSON array of required team roles

    @ElementCollection
    @CollectionTable(name = "template_tags", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    // Default constructor
    public ProjectTemplateEntity() {
    }

    // Constructor with basic fields
    public ProjectTemplateEntity(String name, String description, String category, Long createdById) {
        this.name = name;
        this.description = description;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ProjectPriority getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(ProjectPriority defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public Integer getEstimatedDurationDays() {
        return estimatedDurationDays;
    }

    public void setEstimatedDurationDays(Integer estimatedDurationDays) {
        this.estimatedDurationDays = estimatedDurationDays;
    }

    public Double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Double getDefaultBudget() {
        return defaultBudget;
    }

    public void setDefaultBudget(Double defaultBudget) {
        this.defaultBudget = defaultBudget;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getTaskTemplates() {
        return taskTemplates;
    }

    public void setTaskTemplates(String taskTemplates) {
        this.taskTemplates = taskTemplates;
    }

    public String getMilestoneTemplates() {
        return milestoneTemplates;
    }

    public void setMilestoneTemplates(String milestoneTemplates) {
        this.milestoneTemplates = milestoneTemplates;
    }

    public String getTeamRoles() {
        return teamRoles;
    }

    public void setTeamRoles(String teamRoles) {
        this.teamRoles = teamRoles;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // Helper methods
    public void incrementUsageCount() {
        this.usageCount = (this.usageCount == null ? 0 : this.usageCount) + 1;
    }
}