package com.aipm.ai_project_management.modules.dashboard.dto.admin;

import java.util.List;

public class AdminAiInsightsDTO {
    private List<AdminRiskAlertDTO> riskAlerts;
    private List<AdminDeadlinePredictionDTO> deadlinePredictions;

    // Getters and setters
    public List<AdminRiskAlertDTO> getRiskAlerts() { return riskAlerts; }
    public void setRiskAlerts(List<AdminRiskAlertDTO> riskAlerts) { this.riskAlerts = riskAlerts; }
    public List<AdminDeadlinePredictionDTO> getDeadlinePredictions() { return deadlinePredictions; }
    public void setDeadlinePredictions(List<AdminDeadlinePredictionDTO> deadlinePredictions) { this.deadlinePredictions = deadlinePredictions; }
}
