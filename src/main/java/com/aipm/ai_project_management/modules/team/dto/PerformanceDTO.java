package com.aipm.ai_project_management.modules.team.dto;

import java.time.LocalDateTime;

public class PerformanceDTO {
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Double getOverallScore() {
		return overallScore;
	}
	public void setOverallScore(Double overallScore) {
		this.overallScore = overallScore;
	}
	public Integer getTasksCompleted() {
		return tasksCompleted;
	}
	public void setTasksCompleted(Integer tasksCompleted) {
		this.tasksCompleted = tasksCompleted;
	}
	public Integer getOnTimeDelivery() {
		return onTimeDelivery;
	}
	public void setOnTimeDelivery(Integer onTimeDelivery) {
		this.onTimeDelivery = onTimeDelivery;
	}
	public Double getCodeQuality() {
		return codeQuality;
	}
	public void setCodeQuality(Double codeQuality) {
		this.codeQuality = codeQuality;
	}
	public Double getTeamCollaboration() {
		return teamCollaboration;
	}
	public void setTeamCollaboration(Double teamCollaboration) {
		this.teamCollaboration = teamCollaboration;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	private Long id;
    private Long userId;
    private Double overallScore;
    private Integer tasksCompleted;
    private Integer onTimeDelivery;
    private Double codeQuality;
    private Double teamCollaboration;
    private LocalDateTime createdAt;
    // getters and setters
}