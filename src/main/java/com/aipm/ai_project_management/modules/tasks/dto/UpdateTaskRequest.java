package com.aipm.ai_project_management.modules.tasks.dto;

import com.aipm.ai_project_management.common.enums.TaskPriority;
import com.aipm.ai_project_management.common.enums.TaskStatus;


import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class UpdateTaskRequest {
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public Double getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(Double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Set<String> getLabels() {
		return labels;
	}

	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}

	@Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;
    
    private TaskStatus status;
    
    private TaskPriority priority;
    
    private Long assigneeId;
    
    private LocalDateTime dueDate;
    
    private Double estimatedHours;
    
    private Integer progress;
    
    private Set<String> labels = new HashSet<>();
}
