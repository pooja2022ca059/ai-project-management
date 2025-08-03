package com.aipm.ai_project_management.modules.team.mapper;

import org.springframework.stereotype.Component;

import com.aipm.ai_project_management.modules.team.dto.CertificationDTO;
import com.aipm.ai_project_management.modules.team.entity.UserCertification;


@Component 
public class CertificationMapper {
    public CertificationDTO toDTO(UserCertification entity) {
        if (entity == null) return null;
        CertificationDTO dto = new CertificationDTO();
        dto.setName(entity.getName());
        dto.setIssuer(entity.getIssuer());
        dto.setDate(entity.getDate());
        dto.setExpiry(entity.getExpiry());
        return dto;
    }

    public UserCertification toEntity(CertificationDTO dto) {
        if (dto == null) return null;
        UserCertification entity = new UserCertification();
        entity.setName(dto.getName());
        entity.setIssuer(dto.getIssuer());
        entity.setDate(dto.getDate());
        entity.setExpiry(dto.getExpiry());
        return entity;
    }
}
