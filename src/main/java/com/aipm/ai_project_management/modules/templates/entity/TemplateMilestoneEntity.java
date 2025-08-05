package com.aipm.ai_project_management.modules.templates.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "template_milestones")
public class TemplateMilestoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private ProjectTemplateEntity template;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "days_from_start")
    private Integer daysFromStart; // Days after project start

    @Column(name = "order_index")
    private Integer orderIndex;

    // Constructors
    public TemplateMilestoneEntity() {
    }

    public TemplateMilestoneEntity(ProjectTemplateEntity template, String name, Integer daysFromStart) {
        this.template = template;
        this.name = name;
        this.daysFromStart = daysFromStart;
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

    public Integer getDaysFromStart() {
        return daysFromStart;
    }

    public void setDaysFromStart(Integer daysFromStart) {
        this.daysFromStart = daysFromStart;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}