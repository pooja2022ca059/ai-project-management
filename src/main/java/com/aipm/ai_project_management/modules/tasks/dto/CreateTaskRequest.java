package com.aipm.ai_project_management.modules.tasks.dto;

import com.aipm.ai_project_management.common.enums.TaskPriority;
import com.aipm.ai_project_management.common.enums.TaskStatus;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CreateTaskRequest {
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


	public Long getProjectId() {
		return projectId;
	}


	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}


	public Long getAssigneeId() {
		return assigneeId;
	}


	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}


	public Long getReporterId() {
		return reporterId;
	}


	public void setReporterId(Long reporterId) {
		this.reporterId = reporterId;
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


	public Set<String> getLabels() {
		return labels;
	}


	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}


	public List<Long> getDependencies() {
		return dependencies;
	}


	public void setDependencies(List<Long> dependencies) {
		this.dependencies = dependencies;
	}


	public List<SubtaskRequest> getSubtasks() {
		return subtasks;
	}


	public void setSubtasks(List<SubtaskRequest> subtasks) {
		this.subtasks = subtasks;
	}


	@NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;
    
    private TaskStatus status;
    
    private TaskPriority priority;
    
    private Long projectId;
    
    private Long assigneeId;
    
    @NotNull(message = "Reporter ID is required")
    private Long reporterId;
    
    private LocalDateTime dueDate;
    
    private Double estimatedHours;
    
    private Set<String> labels = new HashSet<>();
    
    private List<Long> dependencies = new ArrayList<>();
    
    private List<SubtaskRequest> subtasks = new ArrayList<>();
    
    
    public static class SubtaskRequest {
        public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Long getAssigneeId() {
			return assigneeId;
		}
		public void setAssigneeId(Long assigneeId) {
			this.assigneeId = assigneeId;
		}
		@NotBlank(message = "Subtask title is required")
        private String title;
        private Long assigneeId;
    }
}
