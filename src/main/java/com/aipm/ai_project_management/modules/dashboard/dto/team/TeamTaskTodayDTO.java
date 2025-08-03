package com.aipm.ai_project_management.modules.dashboard.dto.team;

public class TeamTaskTodayDTO {
    private Long id;
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getEstimatedHours() {
		return estimatedHours;
	}
	public void setEstimatedHours(int double1) {
		this.estimatedHours = double1;
	}
	private String title;
    private String project;
    private String priority;
    private String dueDate;
    private String status;
    private int estimatedHours;

    // Getters and setters...
}
