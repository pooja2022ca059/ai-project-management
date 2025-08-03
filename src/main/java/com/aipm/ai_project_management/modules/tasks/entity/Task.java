package com.aipm.ai_project_management.modules.tasks.entity;

import com.aipm.ai_project_management.common.enums.TaskPriority;
import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.shared.audit.AuditableEntity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "tasks")
public class Task extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

	public Long getReporterId() {
		return reporterId;
	}

	public void setReporterId(Long reporterId) {
		this.reporterId = reporterId;
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

	public List<Subtask> getSubtasks() {
		return subtasks;
	}

	public void setSubtasks(List<Subtask> subtasks) {
		this.subtasks = subtasks;
	}

	public List<TaskComment> getComments() {
		return comments;
	}

	public void setComments(List<TaskComment> comments) {
		this.comments = comments;
	}

	public List<TaskDependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<TaskDependency> dependencies) {
		this.dependencies = dependencies;
	}

	public List<TimeTracking> getTimeLogs() {
		return timeLogs;
	}

	public void setTimeLogs(List<TimeTracking> timeLogs) {
		this.timeLogs = timeLogs;
	}

	public List<String> getAiSuggestions() {
		return aiSuggestions;
	}

	public void setAiSuggestions(List<String> aiSuggestions) {
		this.aiSuggestions = aiSuggestions;
	}

	@Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "estimated_hours")
    private Double estimatedHours;

    @Column(name = "logged_hours")
    private Double loggedHours = 0.0;

    private Integer progress = 0;

    @ElementCollection
    @CollectionTable(name = "task_labels", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "label")
    private Set<String> labels = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subtask> subtasks = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "dependentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskDependency> dependencies = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTracking> timeLogs = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "task_ai_suggestions", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "suggestion", length = 500)
    private List<String> aiSuggestions = new ArrayList<>();
    
    @Column(name = "created_by")
    private Long createdBy;  // Keep as Long if your User IDs are Long

    @Column(name = "updated_by")
    private Long updatedBy;  // Keep as Long if your User IDs are Long
}
