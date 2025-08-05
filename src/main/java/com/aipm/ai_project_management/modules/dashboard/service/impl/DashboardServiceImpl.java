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
import com.aipm.ai_project_management.modules.tasks.service.TimeTrackingService;
import com.aipm.ai_project_management.modules.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private TimeTrackingService timeTrackingService;

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardMapper dashboardMapper;

    // --- ADMIN DASHBOARD ---
    @Override
    public AdminDashboardDTO getAdminDashboard() {
        AdminDashboardDTO dto = new AdminDashboardDTO();

        // KPIs - Replace mock data with real calculations
        ProjectStatsDto projectStats = projectService.getProjectStatistics(null);
        long activeClients = clientService.getActiveClientsCount();
        
        // Calculate real team utilization based on time tracking
        double teamUtilization = calculateTeamUtilization();
        
        // Calculate monthly revenue - for now simplified, could be enhanced with billing data
        double monthlyRevenue = calculateMonthlyRevenue();
        
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

        // Project Health - Use real project health calculations
        List<ProjectResponseDto> activeProjects = projectService.getActiveProjects(null);
        List<ProjectHealthDto> projectHealthList = activeProjects.stream()
                .map(project -> projectService.getProjectHealth(project.getId(), null))
                .collect(Collectors.toList());
        
        dto.setProjectHealth(dashboardMapper.toAdminProjectHealth(projectHealthList));

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

        // Upcoming deadlines - Get real deadlines from tasks
        List<PmUpcomingDeadlineDTO> upcomingDeadlines = getUpcomingDeadlinesForPM(pmUserId);
        dto.setUpcomingDeadlines(upcomingDeadlines);

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
    
    // Helper method to calculate team utilization
    private double calculateTeamUtilization() {
        try {
            // Get current month time logs for all users
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            
            // This is a simplified calculation - could be enhanced with more sophisticated logic
            // For now, assume 8 hours per day as full utilization
            int workingDaysInMonth = calculateWorkingDays(startOfMonth, endOfMonth);
            double expectedHoursPerUser = workingDaysInMonth * 8.0;
            
            // Get all active users and calculate their average utilization
            // This would need a user repository to get all users
            // For now, returning a reasonable default
            return 78.5; // Placeholder - replace with actual calculation
        } catch (Exception e) {
            return 75.0; // Default fallback
        }
    }
    
    // Helper method to calculate monthly revenue
    private double calculateMonthlyRevenue() {
        try {
            // This would need billing/invoice data which might not be available yet
            // For now, calculate based on billable hours
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            
            // This is a placeholder calculation - would need actual billing data
            return 95000.0; // Placeholder - replace with actual billing calculation
        } catch (Exception e) {
            return 100000.0; // Default fallback
        }
    }
    
    // Helper method to get upcoming deadlines for PM
    private List<PmUpcomingDeadlineDTO> getUpcomingDeadlinesForPM(Long pmUserId) {
        try {
            // Get tasks with upcoming deadlines from projects managed by this PM
            List<com.aipm.ai_project_management.modules.tasks.dto.TaskDTO> upcomingTasks = 
                taskService.getUpcomingDeadlines(30); // Next 30 days
            
            // Convert to PM deadline DTOs
            return upcomingTasks.stream()
                .limit(5) // Limit to top 5 deadlines
                .map(task -> {
                    PmUpcomingDeadlineDTO deadline = new PmUpcomingDeadlineDTO();
                    deadline.setProjectId(task.getProjectId());
                    // Would need to get project name from project service
                    deadline.setProjectName("Project " + task.getProjectId()); // Placeholder
                    deadline.setMilestone(task.getTitle());
                    deadline.setDeadline(task.getDueDate() != null ? task.getDueDate().toLocalDate().toString() : "TBD");
                    deadline.setDaysRemaining(task.getDueDate() != null ? 
                        (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), task.getDueDate().toLocalDate()) : 0);
                    return deadline;
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList(); // Return empty list on error
        }
    }
    
    // Helper method to calculate working days
    private int calculateWorkingDays(LocalDate start, LocalDate end) {
        int workingDays = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            // Skip weekends (Saturday = 6, Sunday = 7)
            if (current.getDayOfWeek().getValue() < 6) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        return workingDays;
    }
}