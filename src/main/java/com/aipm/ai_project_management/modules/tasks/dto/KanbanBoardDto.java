package com.aipm.ai_project_management.modules.tasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class KanbanBoardDto {
    
    @JsonProperty("project_id")
    private Long projectId;
    
    @JsonProperty("project_name")
    private String projectName;
    
    private List<KanbanColumn> columns;
    
    // Default constructor
    public KanbanBoardDto() {
    }
    
    // All-args constructor
    public KanbanBoardDto(Long projectId, String projectName, List<KanbanColumn> columns) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.columns = columns;
    }
    
    // Getters and setters
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
    
    public List<KanbanColumn> getColumns() {
        return columns;
    }
    
    public void setColumns(List<KanbanColumn> columns) {
        this.columns = columns;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KanbanBoardDto that = (KanbanBoardDto) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(projectName, that.projectName) &&
                Objects.equals(columns, that.columns);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, columns);
    }
    
    @Override
    public String toString() {
        return "KanbanBoardDto{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", columns=" + columns +
                '}';
    }
    
    // Inner class for Kanban columns
    public static class KanbanColumn {
        private String name;
        private String status;
        private List<KanbanTask> tasks;
        
        @JsonProperty("task_count")
        private int taskCount;
        
        // Default constructor
        public KanbanColumn() {
        }
        
        // All-args constructor
        public KanbanColumn(String name, String status, List<KanbanTask> tasks, int taskCount) {
            this.name = name;
            this.status = status;
            this.tasks = tasks;
            this.taskCount = taskCount;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public List<KanbanTask> getTasks() { return tasks; }
        public void setTasks(List<KanbanTask> tasks) { 
            this.tasks = tasks;
            this.taskCount = tasks != null ? tasks.size() : 0;
        }
        
        public int getTaskCount() { return taskCount; }
        public void setTaskCount(int taskCount) { this.taskCount = taskCount; }
    }
    
    // Inner class for Kanban tasks
    public static class KanbanTask {
        private Long id;
        private String title;
        private String description;
        private String priority;
        private String status;
        
        @JsonProperty("assigned_to")
        private String assignedTo;
        
        @JsonProperty("due_date")
        private String dueDate;
        
        private Integer progress;
        
        @JsonProperty("story_points")
        private Integer storyPoints;
        
        private List<String> tags;
        
        // Default constructor
        public KanbanTask() {
        }
        
        // All-args constructor
        public KanbanTask(Long id, String title, String description, String priority, String status,
                         String assignedTo, String dueDate, Integer progress, Integer storyPoints, 
                         List<String> tags) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.status = status;
            this.assignedTo = assignedTo;
            this.dueDate = dueDate;
            this.progress = progress;
            this.storyPoints = storyPoints;
            this.tags = tags;
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getAssignedTo() { return assignedTo; }
        public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
        
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        
        public Integer getProgress() { return progress; }
        public void setProgress(Integer progress) { this.progress = progress; }
        
        public Integer getStoryPoints() { return storyPoints; }
        public void setStoryPoints(Integer storyPoints) { this.storyPoints = storyPoints; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }
}