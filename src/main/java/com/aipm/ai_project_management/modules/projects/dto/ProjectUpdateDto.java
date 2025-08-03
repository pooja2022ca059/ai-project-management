package com.aipm.ai_project_management.modules.projects.dto;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.common.enums.ProjectStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectUpdateDto {
    
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    
    @Positive(message = "Estimated hours must be positive")
    private Integer estimatedHours;
    
    @Positive(message = "Actual hours must be positive")
    private Integer actualHours;
    
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;
    
    @Positive(message = "Spent amount must be positive")
    private BigDecimal spentAmount;
    
    private Integer progressPercentage;
    private Long clientId;
    private Long projectManagerId;
    
    // Constructors
    public ProjectUpdateDto() {}
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public ProjectStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProjectStatus status) {
        this.status = status;
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
    
    public Integer getEstimatedHours() {
        return estimatedHours;
    }
    
    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }
    
    public Integer getActualHours() {
        return actualHours;
    }
    
    public void setActualHours(Integer actualHours) {
        this.actualHours = actualHours;
    }
    
    public BigDecimal getBudget() {
        return budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
    
    public BigDecimal getSpentAmount() {
        return spentAmount;
    }
    
    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }
    
    public Integer getProgressPercentage() {
        return progressPercentage;
    }
    
    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public Long getProjectManagerId() {
        return projectManagerId;
    }
    
    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

	    @NotNull(message = "Priority is required")
    private ProjectPriority priority; // <-- Add this field

    // Getter & Setter:
    public ProjectPriority getPriority() {
        return priority;
    }
    public void setPriority(ProjectPriority priority) {
        this.priority = priority;
    }
}
