package com.aipm.ai_project_management.modules.dashboard.mapper;

import com.aipm.ai_project_management.modules.dashboard.dto.admin.*;
import com.aipm.ai_project_management.modules.dashboard.dto.pm.*;
import com.aipm.ai_project_management.modules.dashboard.dto.team.*;
import com.aipm.ai_project_management.modules.projects.dto.*;
import com.aipm.ai_project_management.modules.team.dto.*;
import com.aipm.ai_project_management.modules.tasks.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DashboardMapper {

    // ---- Admin ----
    public AdminKpiMetricsDTO toAdminKpiMetrics(ProjectStatsDto stats, long activeClients, double teamUtilization, double monthlyRevenue) {
        AdminKpiMetricsDTO dto = new AdminKpiMetricsDTO();
        dto.setTotalActiveProjects(stats.getActiveProjects() != null ? stats.getActiveProjects().intValue() : 0);
        dto.setTotalClients((int) activeClients);
        dto.setTeamUtilization(teamUtilization);
        dto.setMonthlyRevenue(monthlyRevenue);
        return dto;
    }

    public AdminProjectHealthDTO toAdminProjectHealth(List<ProjectHealthDto> healthList) {
        AdminProjectHealthDTO dto = new AdminProjectHealthDTO();
        int healthy = 0, atRisk = 0, critical = 0;
        for (ProjectHealthDto h : healthList) {
            if ("Healthy".equalsIgnoreCase(h.getOverallHealth())) healthy++;
            else if ("At Risk".equalsIgnoreCase(h.getOverallHealth())) atRisk++;
            else if ("Critical".equalsIgnoreCase(h.getOverallHealth())) critical++;
        }
        dto.setHealthy(healthy);
        dto.setAtRisk(atRisk);
        dto.setCritical(critical);
        return dto;
    }

    // ---- PM ----
    public List<PmProjectDTO> toPmProjects(List<ProjectResponseDto> projects) {
        return projects.stream().map(p -> {
            PmProjectDTO dto = new PmProjectDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setClient(p.getClientName());
            dto.setStatus(p.getStatus().name());
            dto.setProgress(p.getProgressPercentage());
            dto.setDeadline(p.getEndDate() != null ? p.getEndDate().toString() : null);
            dto.setTeamSize(p.getTeamMemberCount() != null ? p.getTeamMemberCount() : 0);
            dto.setHealth("Healthy"); // Or use p.getHealth() if available
            return dto;
        }).collect(Collectors.toList());
    }

    public PmTeamPerformanceDTO toPmTeamPerformance(int totalTasksCompleted, double avgCompletionTime, double teamSatisfaction) {
        PmTeamPerformanceDTO dto = new PmTeamPerformanceDTO();
        dto.setTotalTasksCompleted(totalTasksCompleted);
        dto.setAverageCompletionTime(avgCompletionTime);
        dto.setTeamSatisfaction(teamSatisfaction);
        return dto;
    }

    // ---- Team ----
    public List<TeamTaskTodayDTO> toTeamTaskToday(List<TaskDTO> tasks) {
        return tasks.stream().map(t -> {
            TeamTaskTodayDTO dto = new TeamTaskTodayDTO();
            dto.setId(t.getId());
            dto.setTitle(t.getTitle());
            dto.setProject(t.getProjectName());
            dto.setPriority(t.getPriority().name());
            dto.setDueDate(t.getDueDate() != null ? t.getDueDate().toString() : null);
            dto.setStatus(t.getStatus().name());
            dto.setEstimatedHours(t.getEstimatedHours() != null ? t.getEstimatedHours().intValue() : 0);
            return dto;
        }).collect(Collectors.toList());
    }

    public TeamProductivityMetricsDTO toTeamProductivityMetrics(int tasksCompletedToday, double hoursLogged, int streakDays) {
        TeamProductivityMetricsDTO dto = new TeamProductivityMetricsDTO();
        dto.setTasksCompletedToday(tasksCompletedToday);
        dto.setHoursLogged(hoursLogged);
        dto.setStreakDays(streakDays);
        return dto;
    }

    // ...other mappers as needed
}