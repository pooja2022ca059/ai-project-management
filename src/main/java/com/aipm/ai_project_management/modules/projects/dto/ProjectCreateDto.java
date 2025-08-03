package com.aipm.ai_project_management.modules.projects.dto;

import com.aipm.ai_project_management.common.enums.ProjectPriority;
import com.aipm.ai_project_management.common.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectCreateDto {
    
    @NotBlank(message = "Project name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Project status is required")
    private ProjectStatus status;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @Positive(message = "Estimated hours must be positive")
    private Integer estimatedHours;
    
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;
    
    private Long clientId;
    
    @NotNull(message = "Project manager is required")
    private Long projectManagerId;
    
    @NotNull(message = "Priority is required")
    private ProjectPriority priority; // <-- Add this field

    // Getter & Setter:
    public ProjectPriority getPriority() {
        return priority;
    }
    public void setPriority(ProjectPriority priority) {
        this.priority = priority;
    }
    
    // Constructors
    public ProjectCreateDto() {}
    
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
    
    public BigDecimal getBudget() {
        return budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
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
}
