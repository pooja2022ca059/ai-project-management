package com.aipm.ai_project_management.modules.projects.dto;

public class ProjectHealthDto {
    
    private Long projectId;
    private String projectName;
    private Boolean isOverdue;
    private Double budgetUtilization;
    private Double timeUtilization;
    private String overallHealth; // "Healthy", "Warning", "At Risk", "Critical"
    
    // Constructors
    public ProjectHealthDto() {}
    
    // Getters and Setters
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public Boolean getIsOverdue() {
        return isOverdue;
    }
    
    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }
    
    public Double getBudgetUtilization() {
        return budgetUtilization;
    }
    
    public void setBudgetUtilization(Double budgetUtilization) {
        this.budgetUtilization = budgetUtilization;
    }
    
    public Double getTimeUtilization() {
        return timeUtilization;
    }
    
    public void setTimeUtilization(Double timeUtilization) {
        this.timeUtilization = timeUtilization;
    }
    
    public String getOverallHealth() {
        return overallHealth;
    }
    
    public void setOverallHealth(String overallHealth) {
        this.overallHealth = overallHealth;
    }
}
