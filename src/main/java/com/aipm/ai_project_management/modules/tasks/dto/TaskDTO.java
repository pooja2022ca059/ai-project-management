package com.aipm.ai_project_management.modules.tasks.dto;

import com.aipm.ai_project_management.common.enums.TaskPriority;
import com.aipm.ai_project_management.common.enums.TaskStatus;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TaskDTO {
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
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public UserSummaryDTO getAssignee() {
		return assignee;
	}
	public void setAssignee(UserSummaryDTO assignee) {
		this.assignee = assignee;
	}
	public UserSummaryDTO getReporter() {
		return reporter;
	}
	public void setReporter(UserSummaryDTO reporter) {
		this.reporter = reporter;
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
	public Double getLoggedHours() {
		return loggedHours;
	}
	public void setLoggedHours(Double loggedHours) {
		this.loggedHours = loggedHours;
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
	public List<String> getAiSuggestions() {
		return aiSuggestions;
	}
	public void setAiSuggestions(List<String> aiSuggestions) {
		this.aiSuggestions = aiSuggestions;
	}
	public Integer getDependenciesCount() {
		return dependenciesCount;
	}
	public void setDependenciesCount(Integer dependenciesCount) {
		this.dependenciesCount = dependenciesCount;
	}
	public Integer getSubtasksCount() {
		return subtasksCount;
	}
	public void setSubtasksCount(Integer subtasksCount) {
		this.subtasksCount = subtasksCount;
	}
	public Integer getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
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
	
	private Long assigneeId;
	//private Integer progress;
	//private LocalDateTime updatedAt;

	// Getter-Setters
	public Long getAssigneeId() {
	    return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
	    this.assigneeId = assigneeId;
	}
	private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long projectId;
    private String projectName;
    private UserSummaryDTO assignee;
    private UserSummaryDTO reporter;
    private LocalDateTime dueDate;
    private Double estimatedHours;
    private Double loggedHours;
    private Integer progress;
    private Set<String> labels = new HashSet<>();
    private List<String> aiSuggestions;
    private Integer dependenciesCount;
    private Integer subtasksCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
