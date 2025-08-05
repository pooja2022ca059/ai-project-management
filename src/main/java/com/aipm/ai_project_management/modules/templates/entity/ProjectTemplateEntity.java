package com.aipm.ai_project_management.modules.templates.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemplateCategory category;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @Column(name = "template_created_by", nullable = false)
    private Long templateCreatedBy;

    @Column(name = "usage_count")
    private Long usageCount = 0L;

    @Column(name = "estimated_duration_days")
    private Integer estimatedDurationDays;

    @Column(name = "template_data", columnDefinition = "TEXT")
    private String templateData; // JSON containing template structure

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateTaskEntity> templateTasks = new ArrayList<>();

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateMilestoneEntity> templateMilestones = new ArrayList<>();

    // Constructors
    public ProjectTemplateEntity() {
    }

    public ProjectTemplateEntity(String name, TemplateCategory category, Long templateCreatedBy) {
        this.name = name;
        this.category = category;
        this.templateCreatedBy = templateCreatedBy;
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

    public TemplateCategory getCategory() {
        return category;
    }

    public void setCategory(TemplateCategory category) {
        this.category = category;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Long getTemplateCreatedBy() {
        return templateCreatedBy;
    }

    public void setTemplateCreatedBy(Long templateCreatedBy) {
        this.templateCreatedBy = templateCreatedBy;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public Integer getEstimatedDurationDays() {
        return estimatedDurationDays;
    }

    public void setEstimatedDurationDays(Integer estimatedDurationDays) {
        this.estimatedDurationDays = estimatedDurationDays;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public List<TemplateTaskEntity> getTemplateTasks() {
        return templateTasks;
    }

    public void setTemplateTasks(List<TemplateTaskEntity> templateTasks) {
        this.templateTasks = templateTasks;
    }

    public List<TemplateMilestoneEntity> getTemplateMilestones() {
        return templateMilestones;
    }

    public void setTemplateMilestones(List<TemplateMilestoneEntity> templateMilestones) {
        this.templateMilestones = templateMilestones;
    }

    // Helper methods
    public void incrementUsageCount() {
        this.usageCount++;
    }

    // Enums
    public enum TemplateCategory {
        SOFTWARE_DEVELOPMENT,
        MARKETING_CAMPAIGN,
        PRODUCT_LAUNCH,
        RESEARCH_PROJECT,
        CONSTRUCTION,
        EVENT_PLANNING,
        TRAINING_PROGRAM,
        BUSINESS_PROCESS,
        DESIGN_PROJECT,
        CONSULTING,
        CUSTOM
    }
}