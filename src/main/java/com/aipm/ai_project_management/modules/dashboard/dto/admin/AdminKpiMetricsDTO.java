package com.aipm.ai_project_management.modules.dashboard.dto.admin;

public class AdminKpiMetricsDTO {
    private int totalActiveProjects;
    private int totalClients;
    private double teamUtilization;
    private double monthlyRevenue;

    // Getters and setters
    public int getTotalActiveProjects() { return totalActiveProjects; }
    public void setTotalActiveProjects(int totalActiveProjects) { this.totalActiveProjects = totalActiveProjects; }
    public int getTotalClients() { return totalClients; }
    public void setTotalClients(int totalClients) { this.totalClients = totalClients; }
    public double getTeamUtilization() { return teamUtilization; }
    public void setTeamUtilization(double teamUtilization) { this.teamUtilization = teamUtilization; }
    public double getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(double monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }
}