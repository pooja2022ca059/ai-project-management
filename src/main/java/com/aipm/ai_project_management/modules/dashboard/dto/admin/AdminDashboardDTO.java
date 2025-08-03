package com.aipm.ai_project_management.modules.dashboard.dto.admin;

import java.util.List;

public class AdminDashboardDTO {
    private AdminKpiMetricsDTO kpiMetrics;
    private AdminAiInsightsDTO aiInsights;
    private List<AdminRecentActivityDTO> recentActivities;
    private AdminProjectHealthDTO projectHealth;

    public AdminKpiMetricsDTO getKpiMetrics() { return kpiMetrics; }
    public void setKpiMetrics(AdminKpiMetricsDTO kpiMetrics) { this.kpiMetrics = kpiMetrics; }
    public AdminAiInsightsDTO getAiInsights() { return aiInsights; }
    public void setAiInsights(AdminAiInsightsDTO aiInsights) { this.aiInsights = aiInsights; }
    public List<AdminRecentActivityDTO> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<AdminRecentActivityDTO> recentActivities) { this.recentActivities = recentActivities; }
    public AdminProjectHealthDTO getProjectHealth() { return projectHealth; }
    public void setProjectHealth(AdminProjectHealthDTO projectHealth) { this.projectHealth = projectHealth; }
}