package com.aipm.ai_project_management.modules.dashboard.dto.team;

public class TeamProjectTimelineDTO {
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
	public String getCurrentPhase() {
		return currentPhase;
	}
	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}
	public String getNextMilestone() {
		return nextMilestone;
	}
	public void setNextMilestone(String nextMilestone) {
		this.nextMilestone = nextMilestone;
	}
	public String getMilestoneDate() {
		return milestoneDate;
	}
	public void setMilestoneDate(String milestoneDate) {
		this.milestoneDate = milestoneDate;
	}
	private String projectName;
    private String currentPhase;
    private String nextMilestone;
    private String milestoneDate;

    // Getters and setters...
}
