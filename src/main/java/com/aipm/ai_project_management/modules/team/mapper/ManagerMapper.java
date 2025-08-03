package com.aipm.ai_project_management.modules.team.mapper;

import com.aipm.ai_project_management.modules.team.dto.ManagerDTO;

import org.springframework.stereotype.Component;

import com.aipm.ai_project_management.modules.auth.entity.User;


@Component 
public class ManagerMapper {
    public ManagerDTO toDTO(User user) {
        if (user == null) return null;
        ManagerDTO dto = new ManagerDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
