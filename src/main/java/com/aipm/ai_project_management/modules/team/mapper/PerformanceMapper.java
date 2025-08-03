package com.aipm.ai_project_management.modules.team.mapper;

import com.aipm.ai_project_management.modules.team.dto.PerformanceDTO;
import com.aipm.ai_project_management.modules.team.entity.UserPerformance;

import org.springframework.stereotype.Component;

import com.aipm.ai_project_management.modules.auth.entity.User;


@Component 
public class PerformanceMapper {

    public PerformanceDTO toDTO(UserPerformance entity) {
        if (entity == null) return null;
        PerformanceDTO dto = new PerformanceDTO();
        dto.setId(entity.getId());
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        dto.setOverallScore(entity.getOverallScore());
        dto.setTasksCompleted(entity.getTasksCompleted());
        dto.setOnTimeDelivery(entity.getOnTimeDelivery());
        dto.setCodeQuality(entity.getCodeQuality());
        dto.setTeamCollaboration(entity.getTeamCollaboration());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public UserPerformance toEntity(PerformanceDTO dto) {
        if (dto == null) return null;
        UserPerformance entity = new UserPerformance();
        entity.setId(dto.getId());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            entity.setUser(user);
        }
        entity.setOverallScore(dto.getOverallScore());
        entity.setTasksCompleted(dto.getTasksCompleted());
        entity.setOnTimeDelivery(dto.getOnTimeDelivery());
        entity.setCodeQuality(dto.getCodeQuality());
        entity.setTeamCollaboration(dto.getTeamCollaboration());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}