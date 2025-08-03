package com.aipm.ai_project_management.modules.dashboard.dto.pm;

public class PmTeamPerformanceDTO {
    private int totalTasksCompleted;
    public int getTotalTasksCompleted() {
		return totalTasksCompleted;
	}
	public void setTotalTasksCompleted(int totalTasksCompleted) {
		this.totalTasksCompleted = totalTasksCompleted;
	}
	public double getAverageCompletionTime() {
		return averageCompletionTime;
	}
	public void setAverageCompletionTime(double averageCompletionTime) {
		this.averageCompletionTime = averageCompletionTime;
	}
	public double getTeamSatisfaction() {
		return teamSatisfaction;
	}
	public void setTeamSatisfaction(double teamSatisfaction) {
		this.teamSatisfaction = teamSatisfaction;
	}
	private double averageCompletionTime;
    private double teamSatisfaction;

    // Getters and setters...
}
