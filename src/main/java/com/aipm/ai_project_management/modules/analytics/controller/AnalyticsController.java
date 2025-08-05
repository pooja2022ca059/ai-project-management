package com.aipm.ai_project_management.modules.analytics.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for analytics and reporting features.
 * Provides endpoints for generating reports, charts, and analytics data.
 */
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Analytics and reporting endpoints")
public class AnalyticsController {

    @Operation(summary = "Get project analytics", description = "Get analytics data for a specific project")
    @GetMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ProjectAnalyticsDTO>> getProjectAnalytics(
            @PathVariable Long projectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        // For now, return mock analytics data - will be implemented with real analytics service
        ProjectAnalyticsDTO analytics = new ProjectAnalyticsDTO();
        analytics.setProjectId(projectId);
        analytics.setTotalTasks(25);
        analytics.setCompletedTasks(15);
        analytics.setInProgressTasks(7);
        analytics.setOverdueTasks(3);
        analytics.setTotalHoursLogged(120.5);
        analytics.setBudgetUtilization(75.2);
        analytics.setTeamProductivity(85.0);
        
        // Mock chart data
        analytics.setTaskCompletionTrend(generateMockChartData("Task Completion"));
        analytics.setBurndownChart(generateMockChartData("Burndown"));
        analytics.setTeamPerformance(generateMockTeamPerformance());
        
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }

    @Operation(summary = "Get team analytics", description = "Get analytics data for team performance")
    @GetMapping("/team/{teamId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeamAnalyticsDTO>> getTeamAnalytics(
            @PathVariable Long teamId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        // For now, return mock analytics data - will be implemented with real analytics service
        TeamAnalyticsDTO analytics = new TeamAnalyticsDTO();
        analytics.setTeamId(teamId);
        analytics.setTotalMembers(8);
        analytics.setActiveMembers(7);
        analytics.setAverageProductivity(82.5);
        analytics.setTotalProjectsWorkedOn(5);
        analytics.setTasksCompleted(45);
        analytics.setAverageTaskCompletionTime(2.3); // days
        
        // Mock chart data
        analytics.setProductivityTrend(generateMockChartData("Productivity"));
        analytics.setWorkloadDistribution(generateMockWorkloadData());
        analytics.setSkillMatrix(generateMockSkillMatrix());
        
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }

    @Operation(summary = "Get user analytics", description = "Get analytics data for a specific user")
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<UserAnalyticsDTO>> getUserAnalytics(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        // For now, return mock analytics data - will be implemented with real analytics service
        UserAnalyticsDTO analytics = new UserAnalyticsDTO();
        analytics.setUserId(userId);
        analytics.setTasksAssigned(18);
        analytics.setTasksCompleted(12);
        analytics.setHoursLogged(45.5);
        analytics.setProjectsWorkedOn(3);
        analytics.setAverageTaskCompletionTime(2.1); // days
        analytics.setProductivityScore(88.5);
        
        // Mock chart data
        analytics.setDailyProductivity(generateMockChartData("Daily Productivity"));
        analytics.setTasksByStatus(generateMockTaskStatusData());
        analytics.setTimeTracking(generateMockTimeTrackingData());
        
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }

    @Operation(summary = "Get organization overview", description = "Get high-level organization analytics")
    @GetMapping("/organization/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrganizationAnalyticsDTO>> getOrganizationOverview(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        // For now, return mock analytics data - will be implemented with real analytics service
        OrganizationAnalyticsDTO analytics = new OrganizationAnalyticsDTO();
        analytics.setTotalProjects(45);
        analytics.setActiveProjects(32);
        analytics.setTotalUsers(120);
        analytics.setActiveUsers(95);
        analytics.setTotalTasks(1250);
        analytics.setCompletedTasks(890);
        analytics.setTotalHoursLogged(5250.5);
        analytics.setAverageProjectCompletion(78.5);
        
        // Mock chart data
        analytics.setProjectStatusDistribution(generateMockProjectStatusData());
        analytics.setMonthlyProgress(generateMockChartData("Monthly Progress"));
        analytics.setResourceUtilization(generateMockResourceUtilization());
        
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }

    @Operation(summary = "Generate custom report", description = "Generate a custom report based on specified parameters")
    @PostMapping("/reports/generate")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReportDTO>> generateCustomReport(
            @RequestBody CustomReportRequestDTO request) {
        
        // For now, return mock report data - will be implemented with real reporting service
        ReportDTO report = new ReportDTO();
        report.setId(1L);
        report.setTitle(request.getTitle());
        report.setType(request.getType());
        report.setGeneratedAt(LocalDate.now());
        report.setStatus("COMPLETED");
        report.setDownloadUrl("/api/analytics/reports/1/download");
        
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    // Helper methods to generate mock data
    private List<ChartDataPointDTO> generateMockChartData(String chartType) {
        List<ChartDataPointDTO> data = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            ChartDataPointDTO point = new ChartDataPointDTO();
            point.setLabel("Day " + i);
            point.setValue(Math.random() * 100);
            data.add(point);
        }
        return data;
    }

    private Map<String, Double> generateMockTeamPerformance() {
        Map<String, Double> performance = new HashMap<>();
        performance.put("John Doe", 92.5);
        performance.put("Jane Smith", 88.2);
        performance.put("Bob Johnson", 85.7);
        performance.put("Alice Brown", 91.3);
        return performance;
    }

    private Map<String, Integer> generateMockWorkloadData() {
        Map<String, Integer> workload = new HashMap<>();
        workload.put("John Doe", 8);
        workload.put("Jane Smith", 6);
        workload.put("Bob Johnson", 10);
        workload.put("Alice Brown", 7);
        return workload;
    }

    private Map<String, Object> generateMockSkillMatrix() {
        Map<String, Object> skills = new HashMap<>();
        skills.put("Java", 85);
        skills.put("React", 78);
        skills.put("SQL", 92);
        skills.put("AWS", 65);
        return skills;
    }

    private Map<String, Integer> generateMockTaskStatusData() {
        Map<String, Integer> statusData = new HashMap<>();
        statusData.put("TODO", 3);
        statusData.put("IN_PROGRESS", 5);
        statusData.put("DONE", 12);
        return statusData;
    }

    private List<TimeTrackingDataDTO> generateMockTimeTrackingData() {
        List<TimeTrackingDataDTO> data = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            TimeTrackingDataDTO point = new TimeTrackingDataDTO();
            point.setDate(LocalDate.now().minusDays(7 - i));
            point.setHours(6.0 + Math.random() * 4);
            data.add(point);
        }
        return data;
    }

    private Map<String, Integer> generateMockProjectStatusData() {
        Map<String, Integer> statusData = new HashMap<>();
        statusData.put("PLANNING", 5);
        statusData.put("IN_PROGRESS", 32);
        statusData.put("COMPLETED", 8);
        return statusData;
    }

    private Map<String, Double> generateMockResourceUtilization() {
        Map<String, Double> utilization = new HashMap<>();
        utilization.put("Developers", 85.5);
        utilization.put("Designers", 78.2);
        utilization.put("QA", 92.1);
        utilization.put("DevOps", 88.7);
        return utilization;
    }

    // DTOs for the analytics endpoints would be defined here
    // For brevity, I'll include just the basic structure
    
    public static class ProjectAnalyticsDTO {
        private Long projectId;
        private Integer totalTasks;
        private Integer completedTasks;
        private Integer inProgressTasks;
        private Integer overdueTasks;
        private Double totalHoursLogged;
        private Double budgetUtilization;
        private Double teamProductivity;
        private List<ChartDataPointDTO> taskCompletionTrend;
        private List<ChartDataPointDTO> burndownChart;
        private Map<String, Double> teamPerformance;

        // Getters and setters would be here
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        public Integer getTotalTasks() { return totalTasks; }
        public void setTotalTasks(Integer totalTasks) { this.totalTasks = totalTasks; }
        public Integer getCompletedTasks() { return completedTasks; }
        public void setCompletedTasks(Integer completedTasks) { this.completedTasks = completedTasks; }
        public Integer getInProgressTasks() { return inProgressTasks; }
        public void setInProgressTasks(Integer inProgressTasks) { this.inProgressTasks = inProgressTasks; }
        public Integer getOverdueTasks() { return overdueTasks; }
        public void setOverdueTasks(Integer overdueTasks) { this.overdueTasks = overdueTasks; }
        public Double getTotalHoursLogged() { return totalHoursLogged; }
        public void setTotalHoursLogged(Double totalHoursLogged) { this.totalHoursLogged = totalHoursLogged; }
        public Double getBudgetUtilization() { return budgetUtilization; }
        public void setBudgetUtilization(Double budgetUtilization) { this.budgetUtilization = budgetUtilization; }
        public Double getTeamProductivity() { return teamProductivity; }
        public void setTeamProductivity(Double teamProductivity) { this.teamProductivity = teamProductivity; }
        public List<ChartDataPointDTO> getTaskCompletionTrend() { return taskCompletionTrend; }
        public void setTaskCompletionTrend(List<ChartDataPointDTO> taskCompletionTrend) { this.taskCompletionTrend = taskCompletionTrend; }
        public List<ChartDataPointDTO> getBurndownChart() { return burndownChart; }
        public void setBurndownChart(List<ChartDataPointDTO> burndownChart) { this.burndownChart = burndownChart; }
        public Map<String, Double> getTeamPerformance() { return teamPerformance; }
        public void setTeamPerformance(Map<String, Double> teamPerformance) { this.teamPerformance = teamPerformance; }
    }

    // Additional DTO classes would be defined similarly
    public static class TeamAnalyticsDTO {
        private Long teamId;
        private Integer totalMembers;
        private Integer activeMembers;
        private Double averageProductivity;
        private Integer totalProjectsWorkedOn;
        private Integer tasksCompleted;
        private Double averageTaskCompletionTime;
        private List<ChartDataPointDTO> productivityTrend;
        private Map<String, Integer> workloadDistribution;
        private Map<String, Object> skillMatrix;

        // Getters and setters
        public Long getTeamId() { return teamId; }
        public void setTeamId(Long teamId) { this.teamId = teamId; }
        public Integer getTotalMembers() { return totalMembers; }
        public void setTotalMembers(Integer totalMembers) { this.totalMembers = totalMembers; }
        public Integer getActiveMembers() { return activeMembers; }
        public void setActiveMembers(Integer activeMembers) { this.activeMembers = activeMembers; }
        public Double getAverageProductivity() { return averageProductivity; }
        public void setAverageProductivity(Double averageProductivity) { this.averageProductivity = averageProductivity; }
        public Integer getTotalProjectsWorkedOn() { return totalProjectsWorkedOn; }
        public void setTotalProjectsWorkedOn(Integer totalProjectsWorkedOn) { this.totalProjectsWorkedOn = totalProjectsWorkedOn; }
        public Integer getTasksCompleted() { return tasksCompleted; }
        public void setTasksCompleted(Integer tasksCompleted) { this.tasksCompleted = tasksCompleted; }
        public Double getAverageTaskCompletionTime() { return averageTaskCompletionTime; }
        public void setAverageTaskCompletionTime(Double averageTaskCompletionTime) { this.averageTaskCompletionTime = averageTaskCompletionTime; }
        public List<ChartDataPointDTO> getProductivityTrend() { return productivityTrend; }
        public void setProductivityTrend(List<ChartDataPointDTO> productivityTrend) { this.productivityTrend = productivityTrend; }
        public Map<String, Integer> getWorkloadDistribution() { return workloadDistribution; }
        public void setWorkloadDistribution(Map<String, Integer> workloadDistribution) { this.workloadDistribution = workloadDistribution; }
        public Map<String, Object> getSkillMatrix() { return skillMatrix; }
        public void setSkillMatrix(Map<String, Object> skillMatrix) { this.skillMatrix = skillMatrix; }
    }

    public static class UserAnalyticsDTO {
        private Long userId;
        private Integer tasksAssigned;
        private Integer tasksCompleted;
        private Double hoursLogged;
        private Integer projectsWorkedOn;
        private Double averageTaskCompletionTime;
        private Double productivityScore;
        private List<ChartDataPointDTO> dailyProductivity;
        private Map<String, Integer> tasksByStatus;
        private List<TimeTrackingDataDTO> timeTracking;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Integer getTasksAssigned() { return tasksAssigned; }
        public void setTasksAssigned(Integer tasksAssigned) { this.tasksAssigned = tasksAssigned; }
        public Integer getTasksCompleted() { return tasksCompleted; }
        public void setTasksCompleted(Integer tasksCompleted) { this.tasksCompleted = tasksCompleted; }
        public Double getHoursLogged() { return hoursLogged; }
        public void setHoursLogged(Double hoursLogged) { this.hoursLogged = hoursLogged; }
        public Integer getProjectsWorkedOn() { return projectsWorkedOn; }
        public void setProjectsWorkedOn(Integer projectsWorkedOn) { this.projectsWorkedOn = projectsWorkedOn; }
        public Double getAverageTaskCompletionTime() { return averageTaskCompletionTime; }
        public void setAverageTaskCompletionTime(Double averageTaskCompletionTime) { this.averageTaskCompletionTime = averageTaskCompletionTime; }
        public Double getProductivityScore() { return productivityScore; }
        public void setProductivityScore(Double productivityScore) { this.productivityScore = productivityScore; }
        public List<ChartDataPointDTO> getDailyProductivity() { return dailyProductivity; }
        public void setDailyProductivity(List<ChartDataPointDTO> dailyProductivity) { this.dailyProductivity = dailyProductivity; }
        public Map<String, Integer> getTasksByStatus() { return tasksByStatus; }
        public void setTasksByStatus(Map<String, Integer> tasksByStatus) { this.tasksByStatus = tasksByStatus; }
        public List<TimeTrackingDataDTO> getTimeTracking() { return timeTracking; }
        public void setTimeTracking(List<TimeTrackingDataDTO> timeTracking) { this.timeTracking = timeTracking; }
    }

    public static class OrganizationAnalyticsDTO {
        private Integer totalProjects;
        private Integer activeProjects;
        private Integer totalUsers;
        private Integer activeUsers;
        private Integer totalTasks;
        private Integer completedTasks;
        private Double totalHoursLogged;
        private Double averageProjectCompletion;
        private Map<String, Integer> projectStatusDistribution;
        private List<ChartDataPointDTO> monthlyProgress;
        private Map<String, Double> resourceUtilization;

        // Getters and setters
        public Integer getTotalProjects() { return totalProjects; }
        public void setTotalProjects(Integer totalProjects) { this.totalProjects = totalProjects; }
        public Integer getActiveProjects() { return activeProjects; }
        public void setActiveProjects(Integer activeProjects) { this.activeProjects = activeProjects; }
        public Integer getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }
        public Integer getActiveUsers() { return activeUsers; }
        public void setActiveUsers(Integer activeUsers) { this.activeUsers = activeUsers; }
        public Integer getTotalTasks() { return totalTasks; }
        public void setTotalTasks(Integer totalTasks) { this.totalTasks = totalTasks; }
        public Integer getCompletedTasks() { return completedTasks; }
        public void setCompletedTasks(Integer completedTasks) { this.completedTasks = completedTasks; }
        public Double getTotalHoursLogged() { return totalHoursLogged; }
        public void setTotalHoursLogged(Double totalHoursLogged) { this.totalHoursLogged = totalHoursLogged; }
        public Double getAverageProjectCompletion() { return averageProjectCompletion; }
        public void setAverageProjectCompletion(Double averageProjectCompletion) { this.averageProjectCompletion = averageProjectCompletion; }
        public Map<String, Integer> getProjectStatusDistribution() { return projectStatusDistribution; }
        public void setProjectStatusDistribution(Map<String, Integer> projectStatusDistribution) { this.projectStatusDistribution = projectStatusDistribution; }
        public List<ChartDataPointDTO> getMonthlyProgress() { return monthlyProgress; }
        public void setMonthlyProgress(List<ChartDataPointDTO> monthlyProgress) { this.monthlyProgress = monthlyProgress; }
        public Map<String, Double> getResourceUtilization() { return resourceUtilization; }
        public void setResourceUtilization(Map<String, Double> resourceUtilization) { this.resourceUtilization = resourceUtilization; }
    }

    public static class ChartDataPointDTO {
        private String label;
        private Double value;

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
    }

    public static class TimeTrackingDataDTO {
        private LocalDate date;
        private Double hours;

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public Double getHours() { return hours; }
        public void setHours(Double hours) { this.hours = hours; }
    }

    public static class CustomReportRequestDTO {
        private String title;
        private String type;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> metrics;
        private List<Long> projectIds;
        private List<Long> userIds;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public List<String> getMetrics() { return metrics; }
        public void setMetrics(List<String> metrics) { this.metrics = metrics; }
        public List<Long> getProjectIds() { return projectIds; }
        public void setProjectIds(List<Long> projectIds) { this.projectIds = projectIds; }
        public List<Long> getUserIds() { return userIds; }
        public void setUserIds(List<Long> userIds) { this.userIds = userIds; }
    }

    public static class ReportDTO {
        private Long id;
        private String title;
        private String type;
        private LocalDate generatedAt;
        private String status;
        private String downloadUrl;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public LocalDate getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDate generatedAt) { this.generatedAt = generatedAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    }
}