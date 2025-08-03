package com.aipm.ai_project_management.modules.team.service.impl;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.team.dto.*;
import com.aipm.ai_project_management.modules.team.entity.*;
import com.aipm.ai_project_management.modules.team.mapper.*;
import com.aipm.ai_project_management.modules.team.repository.*;
import com.aipm.ai_project_management.modules.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserCertificationRepository userCertificationRepository;
    private final UserPerformanceRepository userPerformanceRepository;

    private final TeamMapper teamMapper;
    private final TeamMemberMapper teamMemberMapper;
    private final SkillMapper skillMapper;
    private final CertificationMapper certificationMapper;
    private final PerformanceMapper performanceMapper;

    @Autowired
    public TeamServiceImpl(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            UserSkillRepository userSkillRepository,
            UserCertificationRepository userCertificationRepository,
            UserPerformanceRepository userPerformanceRepository,
            TeamMapper teamMapper,
            TeamMemberMapper teamMemberMapper,
            SkillMapper skillMapper,
            CertificationMapper certificationMapper,
            PerformanceMapper performanceMapper
    ) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userSkillRepository = userSkillRepository;
        this.userCertificationRepository = userCertificationRepository;
        this.userPerformanceRepository = userPerformanceRepository;
        this.teamMapper = teamMapper;
        this.teamMemberMapper = teamMemberMapper;
        this.skillMapper = skillMapper;
        this.certificationMapper = certificationMapper;
        this.performanceMapper = performanceMapper;
    }

    // --- TEAM ---
    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team entity = teamMapper.toEntity(teamDTO);
        Team saved = teamRepository.save(entity);
        return teamMapper.toDTO(saved);
    }

    @Override
    public Optional<TeamDTO> getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .map(teamMapper::toDTO);
    }

    @Override
    public Optional<TeamDTO> getTeamByName(String name) {
        return teamRepository.findByName(name)
                .map(teamMapper::toDTO);
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeamDTO updateTeam(Long teamId, TeamDTO updatedTeamDTO) {
        return teamRepository.findById(teamId)
                .map(existing -> {
                    existing.setName(updatedTeamDTO.getName());
                    existing.setDescription(updatedTeamDTO.getDescription());
                    existing.setDepartment(updatedTeamDTO.getDepartment());
                    // ...other fields
                    Team saved = teamRepository.save(existing);
                    return teamMapper.toDTO(saved);
                }).orElseThrow(() -> new RuntimeException("Team not found"));
    }

    @Override
    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    // --- TEAM MEMBERS ---
    @Override
    public List<TeamMemberDTO> listTeamMembers(String search, String role, String status, List<String> skills, int page, int limit) {
        List<TeamMember> members = teamMemberRepository.findAll(); // TODO: filter as per params
        return members.stream()
                .map(teamMemberMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeamMemberDTO getTeamMemberProfile(Long id) {
        return teamMemberRepository.findById(id)
                .map(teamMemberMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Team member not found"));
    }

    @Override
    public TeamMemberDTO addTeamMember(TeamMemberDTO dto) {
        TeamMember entity = teamMemberMapper.toEntity(dto);
        TeamMember saved = teamMemberRepository.save(entity);
        return teamMemberMapper.toDTO(saved);
    }

    @Override
    public TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO dto) {
        return teamMemberRepository.findById(id)
            .map(existing -> {
                existing.setRole(dto.getRole());

                // Handle status safely
                if (dto.getStatus() != null) {
                    try {
                        existing.setStatus(TeamMember.Status.valueOf(dto.getStatus().name()));
                    } catch (IllegalArgumentException e) {
                        existing.setStatus(TeamMember.Status.active); // default or handle as needed
                    }
                } else {
                    existing.setStatus(TeamMember.Status.active); // default or handle as needed
                }

                // Handle availability safely
                if (dto.getAvailability() != null) {
                    try {
                        existing.setAvailability(TeamMember.Availability.valueOf(dto.getAvailability().name()));
                    } catch (IllegalArgumentException e) {
                        existing.setAvailability(TeamMember.Availability.available); // default or handle as needed
                    }
                } else {
                    existing.setAvailability(TeamMember.Availability.available); // default or handle as needed
                }

                // ...other fields (add more updates as needed)
                // existing.setHourlyRate(dto.getHourlyRate()); etc.

                TeamMember saved = teamMemberRepository.save(existing);
                return teamMemberMapper.toDTO(saved);
            })
            .orElseThrow(() -> new RuntimeException("Team member not found"));
    }

    @Override
    public void removeTeamMember(Long teamMemberId) {
        teamMemberRepository.deleteById(teamMemberId);
    }

    // --- SKILLS ---
    @Override
    public List<SkillDTO> getUserSkills(Long userId) {
        return userSkillRepository.findByUserId(userId).stream()
                .map(skillMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SkillDTO addOrUpdateUserSkill(Long userId, SkillDTO skillDTO) {
        UserSkill entity = skillMapper.toEntity(skillDTO);
        entity.setUser(new User(userId, null, null, null, null, null, false, false, null, 0, null, null, false, null));
        UserSkill saved = userSkillRepository.save(entity);
        return skillMapper.toDTO(saved);
    }

    @Override
    public void deleteUserSkill(Long skillId) {
        userSkillRepository.deleteById(skillId);
    }

    // --- CERTIFICATIONS ---
    @Override
    public List<CertificationDTO> getUserCertifications(Long userId) {
        return userCertificationRepository.findByUserId(userId).stream()
                .map(certificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CertificationDTO addOrUpdateUserCertification(Long userId, CertificationDTO certDTO) {
        UserCertification entity = certificationMapper.toEntity(certDTO);
        entity.setUser(new User(userId, null, null, null, null, null, false, false, null, 0, null, null, false, null));
        UserCertification saved = userCertificationRepository.save(entity);
        return certificationMapper.toDTO(saved);
    }

    @Override
    public void deleteUserCertification(Long certId) {
        userCertificationRepository.deleteById(certId);
    }

    // --- PERFORMANCE ---
    @Override
    public List<PerformanceDTO> getUserPerformance(Long userId) {
        return userPerformanceRepository.findByUserId(userId).stream()
                .map(performanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PerformanceDTO addOrUpdateUserPerformance(Long userId, PerformanceDTO perfDTO) {
        UserPerformance entity = performanceMapper.toEntity(perfDTO);
        entity.setUser(new User(userId, null, null, null, null, null, false, false, null, 0, null, null, false, null));
        UserPerformance saved = userPerformanceRepository.save(entity);
        return performanceMapper.toDTO(saved);
    }

    // --- PROJECTS ---
    @Override
    public List<ProjectSummaryDTO> getUserProjects(Long userId) {
        // TODO: implement as per your data model
        return List.of();
    }
}