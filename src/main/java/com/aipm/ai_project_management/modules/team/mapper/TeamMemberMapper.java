package com.aipm.ai_project_management.modules.team.mapper;

import com.aipm.ai_project_management.modules.team.dto.TeamMemberDTO;
import com.aipm.ai_project_management.modules.team.entity.TeamMember;
import com.aipm.ai_project_management.modules.team.entity.Team;
import com.aipm.ai_project_management.modules.auth.entity.User;

import org.springframework.stereotype.Component;

@Component
public class TeamMemberMapper {
    public TeamMemberDTO toDTO(TeamMember entity) {
        if (entity == null) return null;
        TeamMemberDTO dto = new TeamMemberDTO();
        dto.setId(entity.getId());
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setName(entity.getUser().getName());
            dto.setEmail(entity.getUser().getEmail());
            // ...add other user fields as needed
        }
        dto.setRole(entity.getRole());
        dto.setStatus(entity.getStatus() != null ?
            TeamMemberDTO.Status.valueOf(entity.getStatus().name()) : null);
        dto.setAvailability(entity.getAvailability() != null ?
            TeamMemberDTO.Availability.valueOf(entity.getAvailability().name()) : null);
        dto.setHourlyRate(entity.getHourlyRate());
        dto.setCapacity(entity.getCapacity());
        dto.setHireDate(entity.getHireDate());
        if (entity.getTeam() != null)
            dto.setTeamId(entity.getTeam().getId());
        // Map other fields as needed
        return dto;
    }

    public TeamMember toEntity(TeamMemberDTO dto) {
        if (dto == null) return null;
        TeamMember entity = new TeamMember();
        entity.setId(dto.getId());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            entity.setUser(user);
        }
        entity.setRole(dto.getRole());
        // Enum mapping (DTO.Status → Entity.Status)
        if (dto.getStatus() != null) {
            try {
                entity.setStatus(TeamMember.Status.valueOf(dto.getStatus().name()));
            } catch (IllegalArgumentException e) {
                entity.setStatus(TeamMember.Status.active); // default
            }
        } else {
            entity.setStatus(TeamMember.Status.active);
        }
        if (dto.getAvailability() != null) {
            try {
                entity.setAvailability(TeamMember.Availability.valueOf(dto.getAvailability().name()));
            } catch (IllegalArgumentException e) {
                entity.setAvailability(TeamMember.Availability.available); // default
            }
        } else {
            entity.setAvailability(TeamMember.Availability.available);
        }
        if (dto.getTeamId() != null) {
            Team team = new Team();
            team.setId(dto.getTeamId());
            entity.setTeam(team);
        }
        entity.setHourlyRate(dto.getHourlyRate());
        entity.setCapacity(dto.getCapacity());
        entity.setHireDate(dto.getHireDate());
        // Map other fields as needed
        return entity;
    }
}