package com.aipm.ai_project_management.modules.team.mapper;

import com.aipm.ai_project_management.modules.team.dto.TeamDTO;
import com.aipm.ai_project_management.modules.team.dto.TeamMemberDTO;
import com.aipm.ai_project_management.modules.team.entity.Team;
import com.aipm.ai_project_management.modules.team.entity.TeamMember;
import com.aipm.ai_project_management.modules.auth.entity.User;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;


@Component 
public class TeamMapper {

    private final TeamMemberMapper teamMemberMapper;

    public TeamMapper() {
        this.teamMemberMapper = new TeamMemberMapper();
    }

    // Overloaded constructor for dependency injection (if you want to inject a custom mapper)
    public TeamMapper(TeamMemberMapper teamMemberMapper) {
        this.teamMemberMapper = teamMemberMapper;
    }

    public TeamDTO toDTO(Team entity, List<TeamMember> members) {
        if (entity == null) return null;
        TeamDTO dto = new TeamDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDepartment(entity.getDepartment());

        if (entity.getManager() != null) {
            dto.setManagerId(entity.getManager().getId());
            dto.setManagerName(entity.getManager().getName());
        }

        if (members != null) {
            List<TeamMemberDTO> memberDTOs = members
                    .stream()
                    .map(teamMemberMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setMembers(memberDTOs);
        }

        return dto;
    }

    // For cases where you don't want to map members
    public TeamDTO toDTO(Team entity) {
        return toDTO(entity, null);
    }

    public Team toEntity(TeamDTO dto) {
        if (dto == null) return null;
        Team entity = new Team();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDepartment(dto.getDepartment());

        if (dto.getManagerId() != null) {
            User manager = new User();
            manager.setId(dto.getManagerId());
            manager.setName(dto.getManagerName()); // Optional, if you want to set name
            entity.setManager(manager);
        }

        // Members are not set here, as that is usually handled in service/business logic
        return entity;
    }
}