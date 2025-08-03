package com.aipm.ai_project_management.modules.projects.dto;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponseDto {
    
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer estimatedHours;
    private Integer actualHours;
    private BigDecimal budget;
    private BigDecimal spentAmount;
    private Integer progressPercentage;
    private Long clientId;
    private String clientName;
    private Long projectManagerId;
    private String projectManagerName;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProjectMilestoneDto> milestones;
    private List<ProjectTeamMemberDto> teamMembers;
    private Integer totalTasks;
    private Integer completedTasks;
    private Integer teamMemberCount;
    
    // Constructors
    public ProjectResponseDto() {}
    
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
    
    public BigDecimal getBudget() {
        return budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
    
    public BigDecimal getSpentAmount() {
        return spentAmount;
    }
    
    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }
    
    public Integer getProgressPercentage() {
        return progressPercentage;
    }
    
    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public Long getProjectManagerId() {
        return projectManagerId;
    }
    
    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }
    
    public String getProjectManagerName() {
        return projectManagerName;
    }
    
    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getCreatedByName() {
        return createdByName;
    }
    
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
    
    public List<ProjectMilestoneDto> getMilestones() {
        return milestones;
    }
    
    public void setMilestones(List<ProjectMilestoneDto> milestones) {
        this.milestones = milestones;
    }
    
    public List<ProjectTeamMemberDto> getTeamMembers() {
        return teamMembers;
    }
    
    public void setTeamMembers(List<ProjectTeamMemberDto> teamMembers) {
        this.teamMembers = teamMembers;
    }
    
    public Integer getTotalTasks() {
        return totalTasks;
    }
    
    public void setTotalTasks(Integer totalTasks) {
        this.totalTasks = totalTasks;
    }
    
    public Integer getCompletedTasks() {
        return completedTasks;
    }
    
    public void setCompletedTasks(Integer completedTasks) {
        this.completedTasks = completedTasks;
    }
    
    public Integer getTeamMemberCount() {
        return teamMemberCount;
    }
    
    public void setTeamMemberCount(Integer teamMemberCount) {
        this.teamMemberCount = teamMemberCount;
    }
}
