package com.aipm.ai_project_management.modules.team.mapper;

import org.springframework.stereotype.Component;

import com.aipm.ai_project_management.modules.team.dto.SkillDTO;
import com.aipm.ai_project_management.modules.team.entity.UserSkill;



@Component 
public class SkillMapper {
    public SkillDTO toDTO(UserSkill entity) {
        if (entity == null) return null;
        SkillDTO dto = new SkillDTO();
        dto.setName(entity.getName());
        dto.setLevel(entity.getLevel() != null ? entity.getLevel().name() : null);
        dto.setYearsExperience(entity.getYearsExperience());
        return dto;
    }

    public UserSkill toEntity(SkillDTO dto) {
        if (dto == null) return null;
        UserSkill entity = new UserSkill();
        entity.setName(dto.getName());
        // String to Enum conversion, default to beginner if not matched
        try {
            if (dto.getLevel() != null) {
                entity.setLevel(UserSkill.Level.valueOf(dto.getLevel().toLowerCase()));
            }
        } catch (IllegalArgumentException ex) {
            entity.setLevel(UserSkill.Level.beginner);
        }
        entity.setYearsExperience(dto.getYearsExperience());
        return entity;
    }
}
