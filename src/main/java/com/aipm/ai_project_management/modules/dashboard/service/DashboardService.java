package com.aipm.ai_project_management.modules.dashboard.service;

import com.aipm.ai_project_management.modules.dashboard.dto.admin.AdminDashboardDTO;
import com.aipm.ai_project_management.modules.dashboard.dto.pm.PmDashboardDTO;
import com.aipm.ai_project_management.modules.dashboard.dto.team.TeamDashboardDTO;

/**
 * DashboardService provides aggregated dashboard data for different user roles.
 * Each method returns a role-specific DTO matching frontend requirements.
 */
public interface DashboardService {

    /**
     * Get data for the Admin dashboard.
     * @return AdminDashboardDTO containing KPIs, AI insights, recent activities, and project health metrics.
     */
    AdminDashboardDTO getAdminDashboard();

    /**
     * Get data for the Project Manager dashboard.
     * @param pmUserId The user ID of the project manager.
     * @return PmDashboardDTO containing assigned projects, team performance, and upcoming deadlines.
     */
    PmDashboardDTO getPmDashboard(Long pmUserId);

    /**
     * Get data for the Team Member dashboard.
     * @param teamUserId The user ID of the team member.
     * @return TeamDashboardDTO containing today's tasks, project timelines, and productivity metrics.
     */
    TeamDashboardDTO getTeamDashboard(Long teamUserId);

}