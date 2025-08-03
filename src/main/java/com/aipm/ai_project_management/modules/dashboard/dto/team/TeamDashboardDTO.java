package com.aipm.ai_project_management.modules.dashboard.dto.team;

import java.util.List;

public class TeamDashboardDTO {
    private List<TeamTaskTodayDTO> myTasksToday;
    private List<TeamProjectTimelineDTO> projectTimeline;
    private TeamProductivityMetricsDTO productivityMetrics;

    public List<TeamTaskTodayDTO> getMyTasksToday() { return myTasksToday; }
    public void setMyTasksToday(List<TeamTaskTodayDTO> myTasksToday) { this.myTasksToday = myTasksToday; }
    public List<TeamProjectTimelineDTO> getProjectTimeline() { return projectTimeline; }
    public void setProjectTimeline(List<TeamProjectTimelineDTO> projectTimeline) { this.projectTimeline = projectTimeline; }
    public TeamProductivityMetricsDTO getProductivityMetrics() { return productivityMetrics; }
    public void setProductivityMetrics(TeamProductivityMetricsDTO productivityMetrics) { this.productivityMetrics = productivityMetrics; }
}