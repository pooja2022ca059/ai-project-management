package com.aipm.ai_project_management.modules.tasks.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class TimeLogRequest {
    private Long taskId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Hours is required")
    @Positive(message = "Hours must be greater than 0")
    private Double hours;
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    private Boolean billable = true;
    
    // Default constructor
    public TimeLogRequest() {
    }

    // Constructor with fields
    public TimeLogRequest(Long taskId, Long userId, Double hours, String description, LocalDate date, Boolean billable) {
        this.taskId = taskId;
        this.userId = userId;
        this.hours = hours;
        this.description = description;
        this.date = date;
        this.billable = billable;
    }
    
    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Double getHours() {
        return hours;
    }
    
    public void setHours(Double hours) {
        this.hours = hours;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Boolean getBillable() {
        return billable;
    }
    
    public void setBillable(Boolean billable) {
        this.billable = billable;
    }
}