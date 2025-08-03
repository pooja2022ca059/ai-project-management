package com.aipm.ai_project_management.modules.dashboard.dto.team;

public class TeamProductivityMetricsDTO {
    public int getTasksCompletedToday() {
		return tasksCompletedToday;
	}
	public void setTasksCompletedToday(int tasksCompletedToday) {
		this.tasksCompletedToday = tasksCompletedToday;
	}
	public double getHoursLogged() {
		return hoursLogged;
	}
	public void setHoursLogged(double hoursLogged) {
		this.hoursLogged = hoursLogged;
	}
	public int getStreakDays() {
		return streakDays;
	}
	public void setStreakDays(int streakDays) {
		this.streakDays = streakDays;
	}
	private int tasksCompletedToday;
    private double hoursLogged;
    private int streakDays;

    // Getters and setters...
}
