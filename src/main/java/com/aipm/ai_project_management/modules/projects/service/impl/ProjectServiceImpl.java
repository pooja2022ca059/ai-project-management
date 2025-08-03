package com.aipm.ai_project_management.modules.projects.service.impl;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.projects.dto.*;
import com.aipm.ai_project_management.modules.projects.entity.Project;
import com.aipm.ai_project_management.modules.projects.entity.ProjectMilestone;
import com.aipm.ai_project_management.modules.projects.entity.ProjectTeamMember;
import com.aipm.ai_project_management.modules.projects.repository.ProjectRepository;
import com.aipm.ai_project_management.modules.projects.repository.ProjectMilestoneRepository;
import com.aipm.ai_project_management.modules.projects.repository.ProjectTeamMemberRepository;
import com.aipm.ai_project_management.modules.projects.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMilestoneRepository milestoneRepository;

    @Autowired
    private ProjectTeamMemberRepository teamMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ProjectResponseDto createProject(ProjectCreateDto createDto, Long currentUserId) {
    	if (projectRepository.existsByNameIgnoreCase(createDto.getName())) {
            throw new RuntimeException("Project with this name already exists");
        }

        Project project = new Project();
        project.setName(createDto.getName());
        project.setDescription(createDto.getDescription());
        project.setStatus(createDto.getStatus());
        project.setStartDate(createDto.getStartDate());
        project.setEndDate(createDto.getEndDate());
        project.setEstimatedHours(createDto.getEstimatedHours());
        project.setBudget(createDto.getBudget());
        project.setClientId(createDto.getClientId());
        project.setProjectManagerId(createDto.getProjectManagerId());
        project.setPriority(createDto.getPriority()); // <-- ADD THIS LINE
        project.setCreatedBy(currentUserId);
        project.setUpdatedBy(currentUserId);


        Project savedProject = projectRepository.save(project);

        // Add project manager as team member
        if (createDto.getProjectManagerId() != null) {
            ProjectTeamMember manager = new ProjectTeamMember();
            manager.setProject(savedProject);
            manager.setUserId(createDto.getProjectManagerId());
            manager.setRole(UserRole.PROJECT_MANAGER);
            manager.setAssignedBy(currentUserId);
            manager.setJoinedAt(LocalDateTime.now());
            manager.setLeftAt(null); // Active
            teamMemberRepository.save(manager);
        }

        return convertToResponseDto(savedProject);
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, ProjectUpdateDto updateDto, Long currentUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to update this project");
        }

        if (updateDto.getName() != null && !updateDto.getName().equals(project.getName())) {
            if (projectRepository.existsByNameIgnoreCaseAndIdNot(updateDto.getName(), projectId)) {
                throw new RuntimeException("Project with this name already exists");
            }
            project.setName(updateDto.getName());
        }
        
        if (updateDto.getPriority() != null) {
            project.setPriority(updateDto.getPriority()); // <-- ADD THIS LINE
        }

        if (updateDto.getDescription() != null) {
            project.setDescription(updateDto.getDescription());
        }
        if (updateDto.getStatus() != null) {
            project.setStatus(updateDto.getStatus());
        }
        if (updateDto.getStartDate() != null) {
            project.setStartDate(updateDto.getStartDate());
        }
        if (updateDto.getEndDate() != null) {
            project.setEndDate(updateDto.getEndDate());
        }
        if (updateDto.getEstimatedHours() != null) {
            project.setEstimatedHours(updateDto.getEstimatedHours());
        }
        if (updateDto.getActualHours() != null) {
            project.setActualHours(updateDto.getActualHours());
        }
        if (updateDto.getBudget() != null) {
            project.setBudget(updateDto.getBudget());
        }
        if (updateDto.getSpentAmount() != null) {
            project.setSpentAmount(updateDto.getSpentAmount());
        }
        if (updateDto.getProgressPercentage() != null) {
            project.setProgressPercentage(updateDto.getProgressPercentage());
        }
        if (updateDto.getClientId() != null) {
            project.setClientId(updateDto.getClientId());
        }
        if (updateDto.getProjectManagerId() != null) {
            project.setProjectManagerId(updateDto.getProjectManagerId());
        }

        project.setUpdatedBy(currentUserId);

        Project updatedProject = projectRepository.save(project);
        return convertToResponseDto(updatedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long projectId, Long currentUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!hasProjectAccess(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to view this project");
        }

        return convertToResponseDto(project);
    }

    @Override
    public void deleteProject(Long projectId, Long currentUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to delete this project");
        }

        projectRepository.delete(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getAllProjects(Pageable pageable, Long currentUserId) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjectsByStatus(String status, Pageable pageable, Long currentUserId) {
        ProjectStatus projectStatus = ProjectStatus.valueOf(status.toUpperCase());
        Page<Project> projects = projectRepository.findByStatus(projectStatus, pageable);
        return projects.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjectsByClient(Long clientId, Pageable pageable, Long currentUserId) {
        Page<Project> projects = projectRepository.findByClientId(clientId, pageable);
        return projects.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjectsByManager(Long managerId, Pageable pageable, Long currentUserId) {
        Page<Project> projects = projectRepository.findByManagerId(managerId, pageable);
        return projects.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> searchProjectsByName(String name, Pageable pageable, Long currentUserId) {
        Page<Project> projects = projectRepository.findByNameContainingIgnoreCase(name, pageable);
        return projects.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getMyProjects(Long userId, Pageable pageable) {
        Page<Project> projects = projectRepository.findProjectsByTeamMember(userId, pageable);
        return projects.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getActiveProjects(Long currentUserId) {
        Page<Project> projects = projectRepository.findActiveProjects(Pageable.unpaged());
        return projects.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectsEndingSoon(int days, Long currentUserId) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        List<Project> projects = projectRepository.findProjectsEndingSoon(startDate, endDate);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getOverdueProjects(Long currentUserId) {
        LocalDate currentDate = LocalDate.now();
        List<Project> projects = projectRepository.findOverdueProjects(currentDate);
        return projects.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectStatsDto getProjectStatistics(Long currentUserId) {
        ProjectStatsDto stats = new ProjectStatsDto();

        stats.setTotalProjects(projectRepository.count());
        stats.setActiveProjects(projectRepository.countByStatus(ProjectStatus.IN_PROGRESS));
        stats.setCompletedProjects(projectRepository.countByStatus(ProjectStatus.COMPLETED));
        stats.setPlanningProjects(projectRepository.countByStatus(ProjectStatus.PLANNING));
        stats.setOnHoldProjects(projectRepository.countByStatus(ProjectStatus.ON_HOLD));
        stats.setCancelledProjects(projectRepository.countByStatus(ProjectStatus.CANCELLED));

        LocalDate currentDate = LocalDate.now();
        stats.setOverdueProjects((long) projectRepository.findOverdueProjects(currentDate).size());

        return stats;
    }

    @Override
    public ProjectTeamMemberDto addTeamMember(Long projectId, ProjectTeamMemberDto memberDto, Long currentUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to manage team members");
        }

        // Use leftAt IS NULL for "active"
        if (teamMemberRepository.existsActiveByProjectIdAndUserId(projectId, memberDto.getUserId())) {
            throw new RuntimeException("User is already a team member of this project");
        }

        ProjectTeamMember teamMember = new ProjectTeamMember();
        teamMember.setProject(project);
        teamMember.setUserId(memberDto.getUserId());
        teamMember.setRole(memberDto.getRole());
        teamMember.setHourlyRate(memberDto.getHourlyRate());
        teamMember.setAssignedBy(currentUserId);
        teamMember.setJoinedAt(LocalDateTime.now());
        teamMember.setLeftAt(null);

        ProjectTeamMember savedMember = teamMemberRepository.save(teamMember);
        return convertToTeamMemberDto(savedMember);
    }

    @Override
    public ProjectTeamMemberDto updateTeamMember(Long projectId, Long memberId, ProjectTeamMemberDto memberDto, Long currentUserId) {
        ProjectTeamMember teamMember = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to manage team members");
        }

        if (memberDto.getRole() != null) {
            teamMember.setRole(memberDto.getRole());
        }
        if (memberDto.getHourlyRate() != null) {
            teamMember.setHourlyRate(memberDto.getHourlyRate());
        }
        if (memberDto.getIsActive() != null) {
            // Instead of isActive, use leftAt
            if (!memberDto.getIsActive()) {
                teamMember.setLeftAt(LocalDateTime.now());
            } else {
                teamMember.setLeftAt(null);
            }
        }

        ProjectTeamMember updatedMember = teamMemberRepository.save(teamMember);
        return convertToTeamMemberDto(updatedMember);
    }

    @Override
    public void removeTeamMember(Long projectId, Long memberId, Long currentUserId) {
        ProjectTeamMember teamMember = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to manage team members");
        }

        teamMember.setLeftAt(LocalDateTime.now());
        teamMemberRepository.save(teamMember);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectTeamMemberDto> getProjectTeamMembers(Long projectId, Long currentUserId) {
        if (!hasProjectAccess(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to view team members");
        }

        List<ProjectTeamMember> teamMembers = teamMemberRepository.findActiveByProjectId(projectId);
        return teamMembers.stream()
                .map(this::convertToTeamMemberDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectMilestoneDto addMilestone(Long projectId, ProjectMilestoneDto milestoneDto, Long currentUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to manage milestones");
        }

        ProjectMilestone milestone = new ProjectMilestone();
        milestone.setProject(project);
        milestone.setName(milestoneDto.getName());
        milestone.setDescription(milestoneDto.getDescription());
        milestone.setDueDate(milestoneDto.getDueDate());
        milestone.setCreatedBy(currentUserId);
        milestone.setUpdatedBy(currentUserId);

        ProjectMilestone savedMilestone = milestoneRepository.save(milestone);
        return convertToMilestoneDto(savedMilestone);
    }

    @Override
    public ProjectMilestoneDto updateMilestone(Long projectId, Long milestoneId, ProjectMilestoneDto milestoneDto, Long currentUserId) {
        ProjectMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to manage milestones");
        }

        if (milestoneDto.getName() != null) {
            milestone.setName(milestoneDto.getName());
        }
        if (milestoneDto.getDescription() != null) {
            milestone.setDescription(milestoneDto.getDescription());
        }
        if (milestoneDto.getDueDate() != null) {
            milestone.setDueDate(milestoneDto.getDueDate());
        }
        if (milestoneDto.getProgressPercentage() != null) {
            milestone.setProgressPercentage(milestoneDto.getProgressPercentage());
        }

        milestone.setUpdatedBy(currentUserId);

        ProjectMilestone updatedMilestone = milestoneRepository.save(milestone);
        return convertToMilestoneDto(updatedMilestone);
    }

    @Override
    public void deleteMilestone(Long projectId, Long milestoneId, Long currentUserId) {
        ProjectMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to manage milestones");
        }

        milestoneRepository.delete(milestone);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectMilestoneDto> getProjectMilestones(Long projectId, Long currentUserId) {
        if (!hasProjectAccess(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to view milestones");
        }

        Page<ProjectMilestone> milestones = milestoneRepository.findByProjectId(projectId, Pageable.unpaged());
        return milestones.getContent().stream()
                .map(this::convertToMilestoneDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectMilestoneDto completeMilestone(Long projectId, Long milestoneId, Long currentUserId) {
        ProjectMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        if (!hasProjectAccess(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to complete milestones");
        }

        milestone.setIsCompleted(true);
        milestone.setCompletedDate(LocalDate.now());
        milestone.setProgressPercentage(100);
        milestone.setUpdatedBy(currentUserId);

        ProjectMilestone updatedMilestone = milestoneRepository.save(milestone);
        return convertToMilestoneDto(updatedMilestone);
    }

    @Override
    public ProjectResponseDto updateProjectProgress(Long projectId, Integer progressPercentage, Long currentUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!canManageProject(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to update project progress");
        }

        project.setProgressPercentage(progressPercentage);
        project.setUpdatedBy(currentUserId);

        if (progressPercentage == 100) {
            project.setStatus(ProjectStatus.COMPLETED);
        }

        Project updatedProject = projectRepository.save(project);
        return convertToResponseDto(updatedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectHealthDto getProjectHealth(Long projectId, Long currentUserId) {
        if (!hasProjectAccess(projectId, currentUserId)) {
            throw new RuntimeException("Access denied: You don't have permission to view project health");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectHealthDto health = new ProjectHealthDto();
        health.setProjectId(projectId);
        health.setProjectName(project.getName());

        LocalDate currentDate = LocalDate.now();
        boolean isOverdue = project.getEndDate() != null && project.getEndDate().isBefore(currentDate) &&
                !project.getStatus().equals(ProjectStatus.COMPLETED);

        health.setIsOverdue(isOverdue);
        health.setBudgetUtilization(calculateBudgetUtilization(project));
        health.setTimeUtilization(calculateTimeUtilization(project));
        health.setOverallHealth(calculateOverallHealth(project, isOverdue));

        return health;
    }
    
    
    

    private boolean isAdmin(Long userId) {
        return userRepository.findById(userId)
            .map(user -> user.getRole() == UserRole.ADMIN)
            .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasProjectAccess(Long projectId, Long userId) {
        // Allow if user is active team member
        if (teamMemberRepository.existsActiveByProjectIdAndUserId(projectId, userId)) return true;
        // Allow if user is project manager
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null && userId.equals(project.getProjectManagerId())) return true;
        // Optionally, check if user is admin...
        if (isAdmin(userId)) return true;
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canManageProject(Long projectId, Long userId) {
        List<UserRole> userRoles = teamMemberRepository.findUserRolesInProject(projectId, userId);
        if (isAdmin(userId)) return true;
        return userRoles.contains(UserRole.PROJECT_MANAGER) ||userRoles.contains(UserRole.ADMIN);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProjectMember(Long projectId, Long userId) {
        // Returns true if user has ever been a member, regardless of leftAt
        return teamMemberRepository.findByProjectIdAndUserId(projectId, userId).isPresent();
    }

    // Helper conversion methods
    private ProjectResponseDto convertToResponseDto(Project project) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setEstimatedHours(project.getEstimatedHours());
        dto.setActualHours(project.getActualHours());
        dto.setBudget(project.getBudget());
        dto.setSpentAmount(project.getSpentAmount());
        dto.setProgressPercentage(project.getProgressPercentage());
        dto.setClientId(project.getClientId());
        dto.setProjectManagerId(project.getProjectManagerId());
        dto.setCreatedBy(project.getCreatedBy());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());

        // Set team member count using leftAt IS NULL (active members)
        dto.setTeamMemberCount((int) teamMemberRepository.countActiveByProjectId(project.getId()));

        return dto;
    }

    private ProjectTeamMemberDto convertToTeamMemberDto(ProjectTeamMember teamMember) {
        ProjectTeamMemberDto dto = new ProjectTeamMemberDto();
        dto.setId(teamMember.getId());
        dto.setUserId(teamMember.getUserId());
        dto.setRole(teamMember.getRole());
        dto.setAssignedDate(teamMember.getAssignedDate());
        dto.setRemovedDate(teamMember.getLeftAt() == null ? null : teamMember.getLeftAt().toLocalDate());
        dto.setIsActive(teamMember.getLeftAt() == null);
        dto.setHourlyRate(teamMember.getHourlyRate());
        dto.setProjectId(teamMember.getProject().getId());
        dto.setProjectName(teamMember.getProject().getName());
        dto.setAssignedBy(teamMember.getAssignedBy());
        dto.setCreatedAt(teamMember.getCreatedAt());
        dto.setUpdatedAt(teamMember.getUpdatedAt());

        return dto;
    }

    private ProjectMilestoneDto convertToMilestoneDto(ProjectMilestone milestone) {
        ProjectMilestoneDto dto = new ProjectMilestoneDto();
        dto.setId(milestone.getId());
        dto.setName(milestone.getName());
        dto.setDescription(milestone.getDescription());
        dto.setDueDate(milestone.getDueDate());
        dto.setCompletedDate(milestone.getCompletedDate());
        dto.setIsCompleted(milestone.getIsCompleted());
        dto.setProgressPercentage(milestone.getProgressPercentage());
        dto.setProjectId(milestone.getProject().getId());
        dto.setProjectName(milestone.getProject().getName());
        dto.setCreatedBy(milestone.getCreatedBy());
        dto.setCreatedAt(milestone.getCreatedAt());
        dto.setUpdatedAt(milestone.getUpdatedAt());

        return dto;
    }

    // Health calculation helper methods
    private Double calculateBudgetUtilization(Project project) {
        if (project.getBudget() == null || project.getBudget().doubleValue() == 0) {
            return 0.0;
        }
        if (project.getSpentAmount() == null) {
            return 0.0;
        }
        return (project.getSpentAmount().doubleValue() / project.getBudget().doubleValue()) * 100;
    }

    private Double calculateTimeUtilization(Project project) {
        if (project.getEstimatedHours() == null || project.getEstimatedHours() == 0) {
            return 0.0;
        }
        if (project.getActualHours() == null) {
            return 0.0;
        }
        return ((double) project.getActualHours() / project.getEstimatedHours()) * 100;
    }

    private String calculateOverallHealth(Project project, boolean isOverdue) {
        if (isOverdue) {
            return "Critical";
        }

        Double budgetUtil = calculateBudgetUtilization(project);
        Double timeUtil = calculateTimeUtilization(project);

        if (budgetUtil > 90 || timeUtil > 90) {
            return "At Risk";
        } else if (budgetUtil > 75 || timeUtil > 75) {
            return "Warning";
        } else {
            return "Healthy";
        }
    }
}