package com.aipm.ai_project_management.modules.dashboard.dto.pm;

public class PmUpcomingDeadlineDTO {
    private Long projectId;
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
	public String getMilestone() {
		return milestone;
	}
	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public int getDaysRemaining() {
		return daysRemaining;
	}
	public void setDaysRemaining(int daysRemaining) {
		this.daysRemaining = daysRemaining;
	}
	private String projectName;
    private String milestone;
    private String deadline;
    private int daysRemaining;

    // Getters and setters...
}