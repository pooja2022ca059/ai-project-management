package com.aipm.ai_project_management.modules.dashboard.dto.admin;

import java.util.List;

public class AdminRiskAlertDTO {
    private Long id;
    private Long projectId;
    private String projectName;
    private String riskLevel;
    private String message;
    private List<String> suggestedActions;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getSuggestedActions() { return suggestedActions; }
    public void setSuggestedActions(List<String> suggestedActions) { this.suggestedActions = suggestedActions; }
}