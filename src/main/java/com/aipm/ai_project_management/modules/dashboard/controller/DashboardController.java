package com.aipm.ai_project_management.modules.dashboard.controller;

import com.aipm.ai_project_management.modules.dashboard.dto.admin.AdminDashboardDTO;
import com.aipm.ai_project_management.modules.dashboard.dto.pm.PmDashboardDTO;
import com.aipm.ai_project_management.modules.dashboard.dto.team.TeamDashboardDTO;
import com.aipm.ai_project_management.modules.dashboard.service.DashboardService;
import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.auth.security.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AdminDashboardDTO> getAdminDashboard() {
        return ApiResponse.success(dashboardService.getAdminDashboard());
    }

    @GetMapping("/pm")
    @PreAuthorize("hasRole('PM')")
    public ApiResponse<PmDashboardDTO> getPmDashboard(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        return ApiResponse.success(dashboardService.getPmDashboard(userId));
    }

    @GetMapping("/team")
    @PreAuthorize("hasRole('TEAM_MEMBER')")
    public ApiResponse<TeamDashboardDTO> getTeamDashboard(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        return ApiResponse.success(dashboardService.getTeamDashboard(userId));
    }
}