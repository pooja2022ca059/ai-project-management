package com.aipm.ai_project_management.modules.tasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class MoveTaskRequest {
    
    @NotNull(message = "Task ID is required")
    @JsonProperty("task_id")
    private Long taskId;
    
    @NotNull(message = "Target status is required")
    @JsonProperty("target_status")
    private String targetStatus;
    
    @JsonProperty("position")
    private Integer position; // Optional: for ordering within column
    
    // Default constructor
    public MoveTaskRequest() {
    }
    
    // All-args constructor
    public MoveTaskRequest(Long taskId, String targetStatus, Integer position) {
        this.taskId = taskId;
        this.targetStatus = targetStatus;
        this.position = position;
    }
    
    // Getters and setters
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public String getTargetStatus() {
        return targetStatus;
    }
    
    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }
    
    public Integer getPosition() {
        return position;
    }
    
    public void setPosition(Integer position) {
        this.position = position;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveTaskRequest that = (MoveTaskRequest) o;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(targetStatus, that.targetStatus) &&
                Objects.equals(position, that.position);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taskId, targetStatus, position);
    }
    
    @Override
    public String toString() {
        return "MoveTaskRequest{" +
                "taskId=" + taskId +
                ", targetStatus='" + targetStatus + '\'' +
                ", position=" + position +
                '}';
    }
}