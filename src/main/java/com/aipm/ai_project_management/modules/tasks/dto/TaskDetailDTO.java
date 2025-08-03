package com.aipm.ai_project_management.modules.tasks.dto;

import com.aipm.ai_project_management.common.enums.TaskPriority;
import com.aipm.ai_project_management.common.enums.TaskStatus;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TaskDetailDTO {
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

	public ProjectSummaryDTO getProject() {
		return project;
	}

	public void setProject(ProjectSummaryDTO project) {
		this.project = project;
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

	public List<DependencyDTO> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<DependencyDTO> dependencies) {
		this.dependencies = dependencies;
	}

	public List<SubtaskDTO> getSubtasks() {
		return subtasks;
	}

	public void setSubtasks(List<SubtaskDTO> subtasks) {
		this.subtasks = subtasks;
	}

	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public List<TimeTrackingDTO> getTimeLogs() {
		return timeLogs;
	}

	public void setTimeLogs(List<TimeTrackingDTO> timeLogs) {
		this.timeLogs = timeLogs;
	}

	public List<String> getAiSuggestions() {
		return aiSuggestions;
	}

	public void setAiSuggestions(List<String> aiSuggestions) {
		this.aiSuggestions = aiSuggestions;
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
	
	private List<CommentDTO> comments = new ArrayList<>();

	// Getter-Setters
	public List<CommentDTO> getComments() {
	    return comments;
	}

	public void setComments(List<CommentDTO> comments) {
	    this.comments = comments;
	}

	private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    
    private ProjectSummaryDTO project;
    
    private UserSummaryDTO assignee;
    private UserSummaryDTO reporter;
    
    private LocalDateTime dueDate;
    private Double estimatedHours;
    private Double loggedHours;
    private Integer progress;
    
    private Set<String> labels = new HashSet<>();
    private List<DependencyDTO> dependencies = new ArrayList<>();
    private List<SubtaskDTO> subtasks = new ArrayList<>();
    private List<AttachmentDTO> attachments = new ArrayList<>();
    private List<TimeTrackingDTO> timeLogs = new ArrayList<>();
    
    private List<String> aiSuggestions = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
    public static class ProjectSummaryDTO {
        public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private Long id;
        private String name;
    }
    
   
    public static class DependencyDTO {
        public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
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
		public String getDependencyType() {
			return dependencyType;
		}
		public void setDependencyType(String dependencyType) {
			this.dependencyType = dependencyType;
		}
		private Long id;
        private Long taskId;
        private String title;
        private TaskStatus status;
        private String dependencyType;
    }
 
    public static class AttachmentDTO {
        private Long id;
        public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Long getSize() {
			return size;
		}
		public void setSize(Long size) {
			this.size = size;
		}
		public UserSummaryDTO getUploadedBy() {
			return uploadedBy;
		}
		public void setUploadedBy(UserSummaryDTO uploadedBy) {
			this.uploadedBy = uploadedBy;
		}
		public LocalDateTime getUploadedAt() {
			return uploadedAt;
		}
		public void setUploadedAt(LocalDateTime uploadedAt) {
			this.uploadedAt = uploadedAt;
		}
		private String name;
        private String url;
        private Long size;
        private UserSummaryDTO uploadedBy;
        private LocalDateTime uploadedAt;
    }

	public void setComments(ArrayList<CommentDTO> comments2) {
		// TODO Auto-generated method stub
		
	}
}