package com.aipm.ai_project_management.modules.dashboard.service.impl;

import com.aipm.ai_project_management.modules.dashboard.service.DashboardService;
import com.aipm.ai_project_management.modules.dashboard.dto.admin.*;
import com.aipm.ai_project_management.modules.dashboard.dto.pm.*;
import com.aipm.ai_project_management.modules.dashboard.dto.team.*;
import com.aipm.ai_project_management.modules.dashboard.mapper.DashboardMapper;
import com.aipm.ai_project_management.modules.projects.dto.ProjectHealthDto;
import com.aipm.ai_project_management.modules.projects.dto.ProjectStatsDto;
import com.aipm.ai_project_management.modules.projects.dto.ProjectResponseDto;
import com.aipm.ai_project_management.modules.team.dto.TeamMemberDTO;
import com.aipm.ai_project_management.modules.clients.service.ClientService;
import com.aipm.ai_project_management.modules.projects.service.ProjectService;
import com.aipm.ai_project_management.modules.team.service.TeamService;
import com.aipm.ai_project_management.modules.tasks.service.TaskService;
import com.aipm.ai_project_management.modules.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardMapper dashboardMapper;

    // --- ADMIN DASHBOARD ---
    @Override
    public AdminDashboardDTO getAdminDashboard() {
        AdminDashboardDTO dto = new AdminDashboardDTO();

        // KPIs
        ProjectStatsDto projectStats = projectService.getProjectStatistics(null);
        long activeClients = clientService.getActiveClientsCount();
        double teamUtilization = 85.5; // Mocked, replace with actual calculation if available
        double monthlyRevenue = 125000; // Mocked, replace with actual calculation if available
        dto.setKpiMetrics(dashboardMapper.toAdminKpiMetrics(projectStats, activeClients, teamUtilization, monthlyRevenue));

        // AI Insights (mock)
        AdminRiskAlertDTO riskAlert = new AdminRiskAlertDTO();
        riskAlert.setId(1L);
        riskAlert.setProjectId(10L);
        riskAlert.setProjectName("E-commerce Platform");
        riskAlert.setRiskLevel("high");
        riskAlert.setMessage("Project is 15% behind schedule");
        riskAlert.setSuggestedActions(Arrays.asList("Assign additional resources", "Extend deadline"));

        AdminDeadlinePredictionDTO deadlinePrediction = new AdminDeadlinePredictionDTO();
        deadlinePrediction.setProjectId(15L);
        deadlinePrediction.setPredictedCompletion("2024-08-15");
        deadlinePrediction.setConfidence(0.85);

        AdminAiInsightsDTO aiInsights = new AdminAiInsightsDTO();
        aiInsights.setRiskAlerts(Collections.singletonList(riskAlert));
        aiInsights.setDeadlinePredictions(Collections.singletonList(deadlinePrediction));
        dto.setAiInsights(aiInsights);

        // Recent Activities (mock)
        AdminRecentActivityDTO activity = new AdminRecentActivityDTO();
        activity.setId(1L);
        activity.setType("project_created");
        activity.setUser("John Doe");
        activity.setMessage("Created new project: Mobile App");
        activity.setTimestamp("2024-07-08T10:30:00Z");
        dto.setRecentActivities(Collections.singletonList(activity));

        // Project Health
        dto.setProjectHealth(dashboardMapper.toAdminProjectHealth(
            projectService.getActiveProjects(null)
                .stream()
                .map(p -> {
                    ProjectHealthDto ph = new ProjectHealthDto();
                    ph.setOverallHealth("Healthy"); // Mocked, use your own health logic
                    return ph;
                })
                .collect(Collectors.toList())
        ));

        return dto;
    }

    // --- PM DASHBOARD ---
    @Override
    public PmDashboardDTO getPmDashboard(Long pmUserId) {
        PmDashboardDTO dto = new PmDashboardDTO();

        // My projects
        dto.setMyProjects(dashboardMapper.toPmProjects(
            projectService.getProjectsByManager(pmUserId, null, pmUserId).getContent()
        ));

        // Team performance (mocked)
        dto.setTeamPerformance(dashboardMapper.toPmTeamPerformance(156, 2.5, 4.2));

        // Upcoming deadlines (mocked)
        PmUpcomingDeadlineDTO deadline = new PmUpcomingDeadlineDTO();
        deadline.setProjectId(10L);
        deadline.setProjectName("E-commerce Platform");
        deadline.setMilestone("Beta Release");
        deadline.setDeadline("2024-07-15");
        deadline.setDaysRemaining(7);
        dto.setUpcomingDeadlines(Collections.singletonList(deadline));

        return dto;
    }

    // --- TEAM DASHBOARD ---
    @Override
    public TeamDashboardDTO getTeamDashboard(Long teamUserId) {
        TeamDashboardDTO dto = new TeamDashboardDTO();

        // My tasks today
        dto.setMyTasksToday(dashboardMapper.toTeamTaskToday(
            taskService.getTasksAssignedToUser(teamUserId, Collections.emptyMap(), null).getContent()
        ));

        // Project timeline (mocked)
        TeamProjectTimelineDTO timeline = new TeamProjectTimelineDTO();
        timeline.setProjectId(10L);
        timeline.setProjectName("E-commerce Platform");
        timeline.setCurrentPhase("Development");
        timeline.setNextMilestone("Beta Release");
        timeline.setMilestoneDate("2024-07-15");
        dto.setProjectTimeline(Collections.singletonList(timeline));

        // Productivity metrics (mocked)
        dto.setProductivityMetrics(dashboardMapper.toTeamProductivityMetrics(3, 6.5, 12));

        return dto;
    }
}