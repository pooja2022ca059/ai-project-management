package com.aipm.ai_project_management.modules.projects.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.projects.dto.*;
import com.aipm.ai_project_management.modules.projects.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    
    @Autowired
    private ProjectService projectService;
    
    // CREATE PROJECT
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> createProject(
            @Valid @RequestBody ProjectCreateDto createDto) {
        logger.info("POST /api/projects - Creating new project: {}", createDto.getName());
        
        Long currentUserId = getCurrentUserId();
        ProjectResponseDto project = projectService.createProject(createDto, currentUserId);
        
        logger.info("Successfully created project with ID: {}", project.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(project));
    }
    
    // UPDATE PROJECT
    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateDto updateDto) {
        logger.info("PUT /api/projects/{} - Updating project", projectId);
        
        Long currentUserId = getCurrentUserId();
        ProjectResponseDto project = projectService.updateProject(projectId, updateDto, currentUserId);
        
        logger.info("Successfully updated project ID: {}", projectId);
        return ResponseEntity.ok(ApiResponse.success(project));
    }
    
    // GET PROJECT BY ID
    @GetMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(@PathVariable Long projectId) {
        logger.info("GET /api/projects/{} - Fetching project details", projectId);
        
        Long currentUserId = getCurrentUserId();
        ProjectResponseDto project = projectService.getProjectById(projectId, currentUserId);
        
        logger.info("Successfully fetched project ID: {}", projectId);
        return ResponseEntity.ok(ApiResponse.success(project));
    }
    
    // DELETE PROJECT
    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteProject(@PathVariable Long projectId) {
        logger.info("DELETE /api/projects/{} - Deleting project", projectId);
        
        Long currentUserId = getCurrentUserId();
        projectService.deleteProject(projectId, currentUserId);
        
        logger.info("Successfully deleted project ID: {}", projectId);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully"));
    }
    
    // GET ALL PROJECTS (with pagination)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDto>>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        logger.info("GET /api/projects - Fetching all projects (page: {}, size: {})", page, size);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Long currentUserId = getCurrentUserId();
        
        Page<ProjectResponseDto> projects = projectService.getAllProjects(pageable, currentUserId);
        
        logger.info("Successfully fetched {} projects", projects.getTotalElements());
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET PROJECTS BY STATUS
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDto>>> getProjectsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("GET /api/projects/status/{} - Fetching projects by status", status);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Long currentUserId = getCurrentUserId();
        
        Page<ProjectResponseDto> projects = projectService.getProjectsByStatus(status, pageable, currentUserId);
        
        logger.info("Successfully fetched {} projects with status: {}", projects.getTotalElements(), status);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET PROJECTS BY CLIENT
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDto>>> getProjectsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("GET /api/projects/client/{} - Fetching projects by client", clientId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Long currentUserId = getCurrentUserId();
        
        Page<ProjectResponseDto> projects = projectService.getProjectsByClient(clientId, pageable, currentUserId);
        
        logger.info("Successfully fetched {} projects for client ID: {}", projects.getTotalElements(), clientId);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET PROJECTS BY MANAGER
    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDto>>> getProjectsByManager(
            @PathVariable Long managerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("GET /api/projects/manager/{} - Fetching projects by manager", managerId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Long currentUserId = getCurrentUserId();
        
        Page<ProjectResponseDto> projects = projectService.getProjectsByManager(managerId, pageable, currentUserId);
        
        logger.info("Successfully fetched {} projects for manager ID: {}", projects.getTotalElements(), managerId);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // SEARCH PROJECTS BY NAME
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDto>>> searchProjectsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("GET /api/projects/search - Searching projects by name: {}", name);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Long currentUserId = getCurrentUserId();
        
        Page<ProjectResponseDto> projects = projectService.searchProjectsByName(name, pageable, currentUserId);
        
        logger.info("Successfully found {} projects matching name: {}", projects.getTotalElements(), name);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET MY PROJECTS
    @GetMapping("/my-projects")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDto>>> getMyProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("GET /api/projects/my-projects - Fetching current user's projects");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Long currentUserId = getCurrentUserId();
        
        Page<ProjectResponseDto> projects = projectService.getMyProjects(currentUserId, pageable);
        
        logger.info("Successfully fetched {} projects for user ID: {}", projects.getTotalElements(), currentUserId);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET ACTIVE PROJECTS
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getActiveProjects() {
        logger.info("GET /api/projects/active - Fetching active projects");
        
        Long currentUserId = getCurrentUserId();
        List<ProjectResponseDto> projects = projectService.getActiveProjects(currentUserId);
        
        logger.info("Successfully fetched {} active projects", projects.size());
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET PROJECTS ENDING SOON
    @GetMapping("/ending-soon")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsEndingSoon(
            @RequestParam(defaultValue = "7") int days) {
        logger.info("GET /api/projects/ending-soon - Fetching projects ending in {} days", days);
        
        Long currentUserId = getCurrentUserId();
        List<ProjectResponseDto> projects = projectService.getProjectsEndingSoon(days, currentUserId);
        
        logger.info("Successfully fetched {} projects ending soon", projects.size());
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET OVERDUE PROJECTS
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getOverdueProjects() {
        logger.info("GET /api/projects/overdue - Fetching overdue projects");
        
        Long currentUserId = getCurrentUserId();
        List<ProjectResponseDto> projects = projectService.getOverdueProjects(currentUserId);
        
        logger.info("Successfully fetched {} overdue projects", projects.size());
        return ResponseEntity.ok(ApiResponse.success(projects));
    }
    
    // GET PROJECT STATISTICS
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectStatsDto>> getProjectStatistics() {
        logger.info("GET /api/projects/statistics - Fetching project statistics");
        
        Long currentUserId = getCurrentUserId();
        ProjectStatsDto stats = projectService.getProjectStatistics(currentUserId);
        
        logger.info("Successfully fetched project statistics");
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
    
    // PROJECT TEAM MEMBER MANAGEMENT
    
    // ADD TEAM MEMBER
    @PostMapping("/{projectId}/team-members")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectTeamMemberDto>> addTeamMember(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectTeamMemberDto memberDto) {
        logger.info("POST /api/projects/{}/team-members - Adding team member", projectId);
        
        Long currentUserId = getCurrentUserId();
        ProjectTeamMemberDto teamMember = projectService.addTeamMember(projectId, memberDto, currentUserId);
        
        logger.info("Successfully added team member to project ID: {}", projectId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(teamMember));
    }
    
    // UPDATE TEAM MEMBER
    @PutMapping("/{projectId}/team-members/{memberId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectTeamMemberDto>> updateTeamMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @Valid @RequestBody ProjectTeamMemberDto memberDto) {
        logger.info("PUT /api/projects/{}/team-members/{} - Updating team member", projectId, memberId);
        
        Long currentUserId = getCurrentUserId();
        ProjectTeamMemberDto teamMember = projectService.updateTeamMember(projectId, memberId, memberDto, currentUserId);
        
        logger.info("Successfully updated team member ID: {} for project ID: {}", memberId, projectId);
        return ResponseEntity.ok(ApiResponse.success(teamMember));
    }
    
    // REMOVE TEAM MEMBER
    @DeleteMapping("/{projectId}/team-members/{memberId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<String>> removeTeamMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId) {
        logger.info("DELETE /api/projects/{}/team-members/{} - Removing team member", projectId, memberId);
        
        Long currentUserId = getCurrentUserId();
        projectService.removeTeamMember(projectId, memberId, currentUserId);
        
        logger.info("Successfully removed team member ID: {} from project ID: {}", memberId, projectId);
        return ResponseEntity.ok(ApiResponse.success("Team member removed successfully"));
    }
    
    // GET PROJECT TEAM MEMBERS
    @GetMapping("/{projectId}/team-members")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<List<ProjectTeamMemberDto>>> getProjectTeamMembers(
            @PathVariable Long projectId) {
        logger.info("GET /api/projects/{}/team-members - Fetching project team members", projectId);
        
        Long currentUserId = getCurrentUserId();
        List<ProjectTeamMemberDto> teamMembers = projectService.getProjectTeamMembers(projectId, currentUserId);
        
        logger.info("Successfully fetched {} team members for project ID: {}", teamMembers.size(), projectId);
        return ResponseEntity.ok(ApiResponse.success(teamMembers));
    }
    
    // PROJECT MILESTONE MANAGEMENT
    
    // ADD MILESTONE
    @PostMapping("/{projectId}/milestones")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectMilestoneDto>> addMilestone(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMilestoneDto milestoneDto) {
        logger.info("POST /api/projects/{}/milestones - Adding milestone", projectId);
        
        Long currentUserId = getCurrentUserId();
        ProjectMilestoneDto milestone = projectService.addMilestone(projectId, milestoneDto, currentUserId);
        
        logger.info("Successfully added milestone to project ID: {}", projectId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(milestone));
    }
    
    // UPDATE MILESTONE
    @PutMapping("/{projectId}/milestones/{milestoneId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectMilestoneDto>> updateMilestone(
            @PathVariable Long projectId,
            @PathVariable Long milestoneId,
            @Valid @RequestBody ProjectMilestoneDto milestoneDto) {
        logger.info("PUT /api/projects/{}/milestones/{} - Updating milestone", projectId, milestoneId);
        
        Long currentUserId = getCurrentUserId();
        ProjectMilestoneDto milestone = projectService.updateMilestone(projectId, milestoneId, milestoneDto, currentUserId);
        
        logger.info("Successfully updated milestone ID: {} for project ID: {}", milestoneId, projectId);
        return ResponseEntity.ok(ApiResponse.success(milestone));
    }
    
    // DELETE MILESTONE
    @DeleteMapping("/{projectId}/milestones/{milestoneId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<String>> deleteMilestone(
            @PathVariable Long projectId,
            @PathVariable Long milestoneId) {
        logger.info("DELETE /api/projects/{}/milestones/{} - Deleting milestone", projectId, milestoneId);
        
        Long currentUserId = getCurrentUserId();
        projectService.deleteMilestone(projectId, milestoneId, currentUserId);
        
        logger.info("Successfully deleted milestone ID: {} from project ID: {}", milestoneId, projectId);
        return ResponseEntity.ok(ApiResponse.success("Milestone deleted successfully"));
    }
    
    // GET PROJECT MILESTONES
    @GetMapping("/{projectId}/milestones")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<List<ProjectMilestoneDto>>> getProjectMilestones(
            @PathVariable Long projectId) {
        logger.info("GET /api/projects/{}/milestones - Fetching project milestones", projectId);
        
        Long currentUserId = getCurrentUserId();
        List<ProjectMilestoneDto> milestones = projectService.getProjectMilestones(projectId, currentUserId);
        
        logger.info("Successfully fetched {} milestones for project ID: {}", milestones.size(), projectId);
        return ResponseEntity.ok(ApiResponse.success(milestones));
    }
    
    // COMPLETE MILESTONE
    @PatchMapping("/{projectId}/milestones/{milestoneId}/complete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    public ResponseEntity<ApiResponse<ProjectMilestoneDto>> completeMilestone(
            @PathVariable Long projectId,
            @PathVariable Long milestoneId) {
        logger.info("PATCH /api/projects/{}/milestones/{}/complete - Completing milestone", projectId, milestoneId);
        
        Long currentUserId = getCurrentUserId();
        ProjectMilestoneDto milestone = projectService.completeMilestone(projectId, milestoneId, currentUserId);
        
        logger.info("Successfully completed milestone ID: {} for project ID: {}", milestoneId, projectId);
        return ResponseEntity.ok(ApiResponse.success(milestone));
    }
    
    // PROJECT PROGRESS AND HEALTH
    
    // UPDATE PROJECT PROGRESS
    @PatchMapping("/{projectId}/progress")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD')")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProjectProgress(
            @PathVariable Long projectId,
            @RequestParam Integer progressPercentage) {
        logger.info("PATCH /api/projects/{}/progress - Updating project progress to {}%", projectId, progressPercentage);
        
        Long currentUserId = getCurrentUserId();
        ProjectResponseDto project = projectService.updateProjectProgress(projectId, progressPercentage, currentUserId);
        
        logger.info("Successfully updated progress for project ID: {}", projectId);
        return ResponseEntity.ok(ApiResponse.success(project));
    }
    
    // GET PROJECT HEALTH
    @GetMapping("/{projectId}/health")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectHealthDto>> getProjectHealth(@PathVariable Long projectId) {
        logger.info("GET /api/projects/{}/health - Fetching project health", projectId);
        
        Long currentUserId = getCurrentUserId();
        ProjectHealthDto health = projectService.getProjectHealth(projectId, currentUserId);
        
        logger.info("Successfully fetched health for project ID: {}", projectId);
        return ResponseEntity.ok(ApiResponse.success(health));
    }
    
    // GET PROJECT COUNT
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<Long>> getTotalProjectsCount() {
        logger.info("GET /api/projects/count - Fetching total projects count");
        
        Long currentUserId = getCurrentUserId();
        ProjectStatsDto stats = projectService.getProjectStatistics(currentUserId);
        
        logger.info("Total projects count: {}", stats.getTotalProjects());
        return ResponseEntity.ok(ApiResponse.success(stats.getTotalProjects()));
    }
    
    // Helper method to extract user ID from authentication
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            // Extract user ID from authentication principal
            // This depends on your UserDetails implementation
            // For now, returning a placeholder - implement based on your auth setup
            return 1L; // Replace with actual user ID extraction logic
        }
        throw new RuntimeException("User not authenticated");
    }
}