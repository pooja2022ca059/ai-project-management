package com.aipm.ai_project_management.modules.dashboard.dto.admin;

public class AdminDeadlinePredictionDTO {
    private Long projectId;
    private String predictedCompletion;
    private double confidence;

    // Getters and setters
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getPredictedCompletion() { return predictedCompletion; }
    public void setPredictedCompletion(String predictedCompletion) { this.predictedCompletion = predictedCompletion; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
}