package com.aipm.ai_project_management.modules.team.service;

import com.aipm.ai_project_management.modules.team.dto.TeamDTO;
import com.aipm.ai_project_management.modules.team.dto.TeamMemberDTO;
import com.aipm.ai_project_management.modules.team.dto.SkillDTO;
import com.aipm.ai_project_management.modules.team.dto.CertificationDTO;
import com.aipm.ai_project_management.modules.team.dto.PerformanceDTO;
import com.aipm.ai_project_management.modules.team.dto.ProjectSummaryDTO;
import com.aipm.ai_project_management.modules.team.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    // Team operations (use DTOs for API, Entity for internal)
    TeamDTO createTeam(TeamDTO teamDTO);
    Optional<TeamDTO> getTeamById(Long teamId);
    Optional<TeamDTO> getTeamByName(String name);
    List<TeamDTO> getAllTeams();
    TeamDTO updateTeam(Long teamId, TeamDTO updatedTeamDTO);
    void deleteTeam(Long teamId);

    // Team Members (API endpoints)
    List<TeamMemberDTO> listTeamMembers(String search, String role, String status, List<String> skills, int page, int limit);
    TeamMemberDTO getTeamMemberProfile(Long id);
    TeamMemberDTO addTeamMember(TeamMemberDTO dto);
    TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO dto);
    void removeTeamMember(Long teamMemberId);

    // Skills (API endpoints)
    List<SkillDTO> getUserSkills(Long userId);
    SkillDTO addOrUpdateUserSkill(Long userId, SkillDTO skillDTO);
    void deleteUserSkill(Long skillId);

    // Certifications (API endpoints)
    List<CertificationDTO> getUserCertifications(Long userId);
    CertificationDTO addOrUpdateUserCertification(Long userId, CertificationDTO certDTO);
    void deleteUserCertification(Long certId);

    // Project assignments or summaries if needed
    List<ProjectSummaryDTO> getUserProjects(Long userId);

    // Performance (API endpoints)
 // Performance (API endpoints now use DTO)
    List<PerformanceDTO> getUserPerformance(Long userId);
    PerformanceDTO addOrUpdateUserPerformance(Long userId, PerformanceDTO perfDTO);
}