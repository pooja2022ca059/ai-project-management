package com.aipm.ai_project_management.modules.dashboard.dto.pm;

import java.util.List;

public class PmDashboardDTO {
    private List<PmProjectDTO> myProjects;
    private PmTeamPerformanceDTO teamPerformance;
    private List<PmUpcomingDeadlineDTO> upcomingDeadlines;

    public List<PmProjectDTO> getMyProjects() { return myProjects; }
    public void setMyProjects(List<PmProjectDTO> myProjects) { this.myProjects = myProjects; }
    public PmTeamPerformanceDTO getTeamPerformance() { return teamPerformance; }
    public void setTeamPerformance(PmTeamPerformanceDTO teamPerformance) { this.teamPerformance = teamPerformance; }
    public List<PmUpcomingDeadlineDTO> getUpcomingDeadlines() { return upcomingDeadlines; }
    public void setUpcomingDeadlines(List<PmUpcomingDeadlineDTO> upcomingDeadlines) { this.upcomingDeadlines = upcomingDeadlines; }
}