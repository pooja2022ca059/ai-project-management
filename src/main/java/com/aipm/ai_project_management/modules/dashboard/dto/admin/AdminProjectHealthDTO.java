package com.aipm.ai_project_management.modules.dashboard.dto.admin;

public class AdminProjectHealthDTO {
    private int healthy;
    private int atRisk;
    private int critical;

    // Getters and setters
    public int getHealthy() { return healthy; }
    public void setHealthy(int healthy) { this.healthy = healthy; }
    public int getAtRisk() { return atRisk; }
    public void setAtRisk(int atRisk) { this.atRisk = atRisk; }
    public int getCritical() { return critical; }
    public void setCritical(int critical) { this.critical = critical; }
}
