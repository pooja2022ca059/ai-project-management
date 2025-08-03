package com.aipm.ai_project_management.modules.projects.service;

import com.aipm.ai_project_management.modules.projects.dto.*;
import com.aipm.ai_project_management.modules.projects.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    // Basic CRUD operations
    ProjectResponseDto createProject(ProjectCreateDto createDto, Long currentUserId);
    ProjectResponseDto updateProject(Long projectId, ProjectUpdateDto updateDto, Long currentUserId);
    ProjectResponseDto getProjectById(Long projectId, Long currentUserId);
    void deleteProject(Long projectId, Long currentUserId);

    // List and search operations
    Page<ProjectResponseDto> getAllProjects(Pageable pageable, Long currentUserId);
    Page<ProjectResponseDto> getProjectsByStatus(String status, Pageable pageable, Long currentUserId);
    Page<ProjectResponseDto> getProjectsByClient(Long clientId, Pageable pageable, Long currentUserId);
    Page<ProjectResponseDto> getProjectsByManager(Long managerId, Pageable pageable, Long currentUserId);
    Page<ProjectResponseDto> searchProjectsByName(String name, Pageable pageable, Long currentUserId);
    Page<ProjectResponseDto> getMyProjects(Long userId, Pageable pageable);

    // Project analytics and reporting
    List<ProjectResponseDto> getActiveProjects(Long currentUserId);
    List<ProjectResponseDto> getProjectsEndingSoon(int days, Long currentUserId);
    List<ProjectResponseDto> getOverdueProjects(Long currentUserId);
    ProjectStatsDto getProjectStatistics(Long currentUserId);

    // Project team management
    ProjectTeamMemberDto addTeamMember(Long projectId, ProjectTeamMemberDto memberDto, Long currentUserId);
    ProjectTeamMemberDto updateTeamMember(Long projectId, Long memberId, ProjectTeamMemberDto memberDto, Long currentUserId);
    void removeTeamMember(Long projectId, Long memberId, Long currentUserId);
    List<ProjectTeamMemberDto> getProjectTeamMembers(Long projectId, Long currentUserId);

    // Project milestone management
    ProjectMilestoneDto addMilestone(Long projectId, ProjectMilestoneDto milestoneDto, Long currentUserId);
    ProjectMilestoneDto updateMilestone(Long projectId, Long milestoneId, ProjectMilestoneDto milestoneDto, Long currentUserId);
    void deleteMilestone(Long projectId, Long milestoneId, Long currentUserId);
    List<ProjectMilestoneDto> getProjectMilestones(Long projectId, Long currentUserId);
    ProjectMilestoneDto completeMilestone(Long projectId, Long milestoneId, Long currentUserId);

    // Project progress and health
    ProjectResponseDto updateProjectProgress(Long projectId, Integer progressPercentage, Long currentUserId);
    ProjectHealthDto getProjectHealth(Long projectId, Long currentUserId);

    // Access control helpers

    /**
     * Checks if the user is an active member of the project.
     * Implementation should use leftAt IS NULL to determine activity.
     */
    boolean hasProjectAccess(Long projectId, Long userId);

    /**
     * Checks if the user can manage the project.
     * Typically means they are the manager (use managerId) or have a specific role.
     */
    boolean canManageProject(Long projectId, Long userId);

    /**
     * Checks if the user has ever been a member of the project (active or inactive).
     */
    boolean isProjectMember(Long projectId, Long userId);
}