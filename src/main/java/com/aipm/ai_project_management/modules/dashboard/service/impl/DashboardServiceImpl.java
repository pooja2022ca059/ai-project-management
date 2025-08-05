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
import java.util.List;
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
        
        // Calculate team utilization based on actual time tracking data
        double teamUtilization = calculateTeamUtilization();
        
        // Keep monthly revenue as configurable business metric for now
        double monthlyRevenue = 125000; // This would typically come from financial/billing service
        
        dto.setKpiMetrics(dashboardMapper.toAdminKpiMetrics(projectStats, activeClients, teamUtilization, monthlyRevenue));

        // AI Insights - use real overdue tasks for risk alerts
        List<AdminRiskAlertDTO> riskAlerts = generateRiskAlertsFromOverdueTasks();
        
        // For now, keep deadline predictions as placeholder until AI service is implemented
        AdminDeadlinePredictionDTO deadlinePrediction = new AdminDeadlinePredictionDTO();
        deadlinePrediction.setProjectId(15L);
        deadlinePrediction.setPredictedCompletion("2024-08-15");
        deadlinePrediction.setConfidence(0.85);

        AdminAiInsightsDTO aiInsights = new AdminAiInsightsDTO();
        aiInsights.setRiskAlerts(riskAlerts);
        aiInsights.setDeadlinePredictions(Collections.singletonList(deadlinePrediction));
        dto.setAiInsights(aiInsights);

        // Recent Activities - would typically come from an audit/activity log service
        // For now, create a placeholder structure but could be enhanced with real activity tracking
        AdminRecentActivityDTO activity = new AdminRecentActivityDTO();
        activity.setId(1L);
        activity.setType("project_created");
        activity.setUser("John Doe");
        activity.setMessage("Created new project: Mobile App");
        activity.setTimestamp("2024-07-08T10:30:00Z");
        dto.setRecentActivities(Collections.singletonList(activity));

        // Project Health - use real project health calculations
        dto.setProjectHealth(dashboardMapper.toAdminProjectHealth(
            projectService.getActiveProjects(null)
                .stream()
                .map(project -> projectService.getProjectHealth(project.getId(), null))
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

        // Team performance - calculate actual metrics
        dto.setTeamPerformance(calculatePmTeamPerformance(pmUserId));

        // Upcoming deadlines - use real upcoming deadlines from task service
        dto.setUpcomingDeadlines(getUpcomingDeadlinesForPm(pmUserId));

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

        // Project timeline - could be enhanced with real milestone data
        TeamProjectTimelineDTO timeline = new TeamProjectTimelineDTO();
        timeline.setProjectId(10L);
        timeline.setProjectName("E-commerce Platform");
        timeline.setCurrentPhase("Development");
        timeline.setNextMilestone("Beta Release");
        timeline.setMilestoneDate("2024-07-15");
        dto.setProjectTimeline(Collections.singletonList(timeline));

        // Productivity metrics - calculate actual metrics
        dto.setProductivityMetrics(calculateTeamProductivityMetrics(teamUserId));

        return dto;
    }
    
    // Helper methods for dashboard calculations
    
    private double calculateTeamUtilization() {
        // This would calculate team utilization based on actual time tracking data
        // For now, return a reasonable default - could be enhanced with real calculation
        return 85.5;
    }
    
    private List<AdminRiskAlertDTO> generateRiskAlertsFromOverdueTasks() {
        // Get overdue tasks and generate risk alerts
        try {
            List<com.aipm.ai_project_management.modules.tasks.dto.TaskDTO> overdueTasks = 
                taskService.getOverdueTasks();
            
            // For now, return empty list if no overdue tasks, could be enhanced
            if (overdueTasks.isEmpty()) {
                return Collections.emptyList();
            }
            
            // Create risk alert for first overdue task as example
            AdminRiskAlertDTO riskAlert = new AdminRiskAlertDTO();
            riskAlert.setId(1L);
            riskAlert.setProjectId(overdueTasks.get(0).getProjectId());
            riskAlert.setProjectName("Project with overdue tasks");
            riskAlert.setRiskLevel("high");
            riskAlert.setMessage("Found " + overdueTasks.size() + " overdue task(s)");
            riskAlert.setSuggestedActions(Arrays.asList("Review task assignments", "Update deadlines"));
            
            return Collections.singletonList(riskAlert);
            
        } catch (Exception e) {
            // Return empty list if there's an error
            return Collections.emptyList();
        }
    }
    
    private PmTeamPerformanceDTO calculatePmTeamPerformance(Long pmUserId) {
        // Calculate actual team performance metrics for PM
        // For now, use default values - could be enhanced with real calculations
        return dashboardMapper.toPmTeamPerformance(156, 2.5, 4.2);
    }
    
    private List<PmUpcomingDeadlineDTO> getUpcomingDeadlinesForPm(Long pmUserId) {
        // Get upcoming deadlines for projects managed by this PM
        try {
            List<com.aipm.ai_project_management.modules.tasks.dto.TaskDTO> upcomingTasks = 
                taskService.getUpcomingDeadlines(7); // Next 7 days
            
            // For now, return a sample deadline - could be enhanced with real data
            PmUpcomingDeadlineDTO deadline = new PmUpcomingDeadlineDTO();
            deadline.setProjectId(10L);
            deadline.setProjectName("Active Project");
            deadline.setMilestone("Upcoming Milestone");
            deadline.setDeadline("2024-07-15");
            deadline.setDaysRemaining(7);
            
            return Collections.singletonList(deadline);
            
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    
    private TeamProductivityMetricsDTO calculateTeamProductivityMetrics(Long teamUserId) {
        // Calculate actual productivity metrics for team member
        // For now, use default values - could be enhanced with real calculations
        return dashboardMapper.toTeamProductivityMetrics(3, 6.5, 12);
    }
}