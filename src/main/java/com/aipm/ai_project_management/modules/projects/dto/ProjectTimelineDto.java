package com.aipm.ai_project_management.modules.projects.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ProjectTimelineDto {
    
    private Long id;
    private String name;
    
    @JsonProperty("start_date")
    private LocalDate startDate;
    
    @JsonProperty("end_date") 
    private LocalDate endDate;
    
    private String status;
    private Double progress;
    
    private List<TimelineTask> tasks;
    private List<TimelineMilestone> milestones;
    
    // Constructor
    public ProjectTimelineDto() {
    }
    
    public ProjectTimelineDto(Long id, String name, LocalDate startDate, LocalDate endDate, 
                             String status, Double progress, List<TimelineTask> tasks, 
                             List<TimelineMilestone> milestones) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.progress = progress;
        this.tasks = tasks;
        this.milestones = milestones;
    }
    
    // Getters and setters
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
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getProgress() {
        return progress;
    }
    
    public void setProgress(Double progress) {
        this.progress = progress;
    }
    
    public List<TimelineTask> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<TimelineTask> tasks) {
        this.tasks = tasks;
    }
    
    public List<TimelineMilestone> getMilestones() {
        return milestones;
    }
    
    public void setMilestones(List<TimelineMilestone> milestones) {
        this.milestones = milestones;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectTimelineDto that = (ProjectTimelineDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(status, that.status) &&
                Objects.equals(progress, that.progress) &&
                Objects.equals(tasks, that.tasks) &&
                Objects.equals(milestones, that.milestones);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, endDate, status, progress, tasks, milestones);
    }
    
    @Override
    public String toString() {
        return "ProjectTimelineDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", progress=" + progress +
                ", tasks=" + tasks +
                ", milestones=" + milestones +
                '}';
    }
    
    // Inner classes for timeline items
    public static class TimelineTask {
        private Long id;
        private String name;
        private String description;
        
        @JsonProperty("start_date")
        private LocalDate startDate;
        
        @JsonProperty("end_date")
        private LocalDate endDate;
        
        private String status;
        private Double progress;
        private String priority;
        
        @JsonProperty("assigned_to")
        private String assignedTo;
        
        private List<Long> dependencies;
        
        // Default constructor
        public TimelineTask() {
        }
        
        // All-args constructor
        public TimelineTask(Long id, String name, String description, LocalDate startDate, 
                           LocalDate endDate, String status, Double progress, String priority, 
                           String assignedTo, List<Long> dependencies) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
            this.progress = progress;
            this.priority = priority;
            this.assignedTo = assignedTo;
            this.dependencies = dependencies;
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Double getProgress() { return progress; }
        public void setProgress(Double progress) { this.progress = progress; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public String getAssignedTo() { return assignedTo; }
        public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
        
        public List<Long> getDependencies() { return dependencies; }
        public void setDependencies(List<Long> dependencies) { this.dependencies = dependencies; }
    }
    
    public static class TimelineMilestone {
        private Long id;
        private String name;
        private String description;
        
        @JsonProperty("due_date")
        private LocalDate dueDate;
        
        private String status;
        
        @JsonProperty("completed_date")
        private LocalDate completedDate;
        
        // Default constructor
        public TimelineMilestone() {
        }
        
        // All-args constructor
        public TimelineMilestone(Long id, String name, String description, LocalDate dueDate, 
                                String status, LocalDate completedDate) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.dueDate = dueDate;
            this.status = status;
            this.completedDate = completedDate;
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public LocalDate getCompletedDate() { return completedDate; }
        public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }
    }
}