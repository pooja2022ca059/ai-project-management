package com.aipm.ai_project_management.modules.projects.entity;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.common.enums.ProjectRiskLevel;
import com.aipm.ai_project_management.common.enums.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Project name is required")
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Project status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;
    
    
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    // Map to actual database column 'progress' instead of 'progress_percentage'
    @Column(name = "progress", precision = 5, scale = 2)
    private BigDecimal progress;
    
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;
    
    // Map to actual database column 'spent' instead of 'spent_amount'
    @Column(name = "spent", precision = 15, scale = 2)
    private BigDecimal spent;
    
    @Column(name = "client_id")
    private Long clientId;
    
    // Map to actual database column 'manager_id' instead of 'project_manager_id'
    @Column(name = "manager_id")
    private Long managerId;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "updated_by")
    private Long updatedBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Add missing fields from schema
    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;
    
    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ProjectPriority priority;
    
    @Column(name = "ai_health_score", precision = 3, scale = 1)
    private BigDecimal aiHealthScore;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_risk_level")
    private ProjectRiskLevel aiRiskLevel;
    
    @Column(name = "tags", columnDefinition = "JSON")
    private String tags;
    
    @Column(name = "custom_fields", columnDefinition = "JSON")
    private String customFields;
    
    // Remove these fields as they don't exist in database
    // @Column(name = "estimated_hours")
    // private Integer estimatedHours;
    
    // @Column(name = "actual_hours")
    // private Integer actualHours;
    
    // If you need these fields for business logic, mark them as @Transient
    @Transient
    private Integer estimatedHours;
    
    @Transient
    private Integer actualHours;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectMilestone> milestones = new HashSet<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectTeamMember> teamMembers = new HashSet<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectDocument> documents = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.progress == null) {
            this.progress = BigDecimal.ZERO;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Project() {}
    
    public Project(String name, String description, ProjectStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
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
    
    public ProjectStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public BigDecimal getProgress() {
        return progress;
    }
    
    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }
    
    public BigDecimal getBudget() {
        return budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
    
    public BigDecimal getSpent() {
        return spent;
    }
    
    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public Long getManagerId() {
        return managerId;
    }
    
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public Long getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
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
    
    public LocalDate getActualStartDate() {
        return actualStartDate;
    }
    
    public void setActualStartDate(LocalDate actualStartDate) {
        this.actualStartDate = actualStartDate;
    }
    
    public LocalDate getActualEndDate() {
        return actualEndDate;
    }
    
    public void setActualEndDate(LocalDate actualEndDate) {
        this.actualEndDate = actualEndDate;
    }
    
    public ProjectPriority getPriority() {
        return priority;
    }
    
    public void setPriority(ProjectPriority priority) {
        this.priority = priority;
    }
    
    public BigDecimal getAiHealthScore() {
        return aiHealthScore;
    }
    
    public void setAiHealthScore(BigDecimal aiHealthScore) {
        this.aiHealthScore = aiHealthScore;
    }
    
    public ProjectRiskLevel getAiRiskLevel() {
        return aiRiskLevel;
    }
    
    public void setAiRiskLevel(ProjectRiskLevel aiRiskLevel) {
        this.aiRiskLevel = aiRiskLevel;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public String getCustomFields() {
        return customFields;
    }
    
    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }
    
    // Transient fields getters/setters
    public Integer getEstimatedHours() {
        return estimatedHours;
    }
    
    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }
    
    public Integer getActualHours() {
        return actualHours;
    }
    
    public void setActualHours(Integer actualHours) {
        this.actualHours = actualHours;
    }
    
    public Set<ProjectMilestone> getMilestones() {
        return milestones;
    }
    
    public void setMilestones(Set<ProjectMilestone> milestones) {
        this.milestones = milestones;
    }
    
    public Set<ProjectTeamMember> getTeamMembers() {
        return teamMembers;
    }
    
    public void setTeamMembers(Set<ProjectTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }
    
    public Set<ProjectDocument> getDocuments() {
        return documents;
    }
    
    public void setDocuments(Set<ProjectDocument> documents) {
        this.documents = documents;
    }
    
    // Backward compatibility methods
    public Integer getProgressPercentage() {
        return progress != null ? progress.intValue() : 0;
    }
    
    public void setProgressPercentage(Integer progressPercentage) {
        this.progress = progressPercentage != null ? BigDecimal.valueOf(progressPercentage) : BigDecimal.ZERO;
    }
    
    public BigDecimal getSpentAmount() {
        return spent;
    }
    
    public void setSpentAmount(BigDecimal spentAmount) {
        this.spent = spentAmount;
    }
    
    public Long getProjectManagerId() {
        return managerId;
    }
    
    public void setProjectManagerId(Long projectManagerId) {
        this.managerId = projectManagerId;
    }
}