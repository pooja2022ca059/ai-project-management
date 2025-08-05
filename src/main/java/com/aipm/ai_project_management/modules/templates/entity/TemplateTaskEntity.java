package com.aipm.ai_project_management.modules.templates.entity;

import com.aipm.ai_project_management.common.enums.TaskPriority;
import jakarta.persistence.*;

@Entity
@Table(name = "template_tasks")
public class TemplateTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private ProjectTemplateEntity template;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "estimated_hours")
    private Double estimatedHours;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "depends_on_task_name")
    private String dependsOnTaskName; // Name of predecessor task

    @Column(name = "days_from_start")
    private Integer daysFromStart; // Days after project start

    // Constructors
    public TemplateTaskEntity() {
    }

    public TemplateTaskEntity(ProjectTemplateEntity template, String name) {
        this.template = template;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectTemplateEntity getTemplate() {
        return template;
    }

    public void setTemplate(ProjectTemplateEntity template) {
        this.template = template;
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

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getDependsOnTaskName() {
        return dependsOnTaskName;
    }

    public void setDependsOnTaskName(String dependsOnTaskName) {
        this.dependsOnTaskName = dependsOnTaskName;
    }

    public Integer getDaysFromStart() {
        return daysFromStart;
    }

    public void setDaysFromStart(Integer daysFromStart) {
        this.daysFromStart = daysFromStart;
    }
}