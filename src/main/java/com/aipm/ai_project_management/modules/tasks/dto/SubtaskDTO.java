package com.aipm.ai_project_management.modules.tasks.dto;

import com.aipm.ai_project_management.common.enums.TaskStatus;


import java.time.LocalDateTime;

/**
 * DTO representing a subtask within a parent task.
 */

public class SubtaskDTO {
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
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public UserSummaryDTO getAssignee() {
		return assignee;
	}
	public void setAssignee(UserSummaryDTO assignee) {
		this.assignee = assignee;
	}
	public LocalDateTime getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
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
	
	private Boolean completed;

	// Getter-Setter
	public Boolean getCompleted() {
	    return completed;
	}

	public void setCompleted(Boolean completed) {
	    this.completed = completed;
	}
	private String title;
    private TaskStatus status;
    private Long taskId;
    private UserSummaryDTO assignee;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	public void setCompleted(boolean completed2) {
		// TODO Auto-generated method stub
		
	}
    
	
}
