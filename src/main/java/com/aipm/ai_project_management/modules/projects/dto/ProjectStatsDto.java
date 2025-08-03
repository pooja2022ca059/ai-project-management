package com.aipm.ai_project_management.modules.projects.dto;

public class ProjectStatsDto {
    
    private Long totalProjects;
    private Long activeProjects;
    private Long completedProjects;
    private Long planningProjects;
    private Long onHoldProjects;
    private Long cancelledProjects;
    private Long overdueProjects;
    
    // Constructors
    public ProjectStatsDto() {}
    
    // Getters and Setters
    public Long getTotalProjects() {
        return totalProjects;
    }
    
    public void setTotalProjects(Long totalProjects) {
        this.totalProjects = totalProjects;
    }
    
    public Long getActiveProjects() {
        return activeProjects;
    }
    
    public void setActiveProjects(Long activeProjects) {
        this.activeProjects = activeProjects;
    }
    
    public Long getCompletedProjects() {
        return completedProjects;
    }
    
    public void setCompletedProjects(Long completedProjects) {
        this.completedProjects = completedProjects;
    }
    
    public Long getPlanningProjects() {
        return planningProjects;
    }
    
    public void setPlanningProjects(Long planningProjects) {
        this.planningProjects = planningProjects;
    }
    
    public Long getOnHoldProjects() {
        return onHoldProjects;
    }
    
    public void setOnHoldProjects(Long onHoldProjects) {
        this.onHoldProjects = onHoldProjects;
    }
    
    public Long getCancelledProjects() {
        return cancelledProjects;
    }
    
    public void setCancelledProjects(Long cancelledProjects) {
        this.cancelledProjects = cancelledProjects;
    }
    
    public Long getOverdueProjects() {
        return overdueProjects;
    }
    
    public void setOverdueProjects(Long overdueProjects) {
        this.overdueProjects = overdueProjects;
    }
}
