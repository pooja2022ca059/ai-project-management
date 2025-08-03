package com.aipm.ai_project_management.modules.team.mapper;

import com.aipm.ai_project_management.modules.team.dto.ProjectSummaryDTO;

import org.springframework.stereotype.Component;

import com.aipm.ai_project_management.modules.projects.entity.Project;


@Component 
public class ProjectSummaryMapper {

    public ProjectSummaryDTO toDTO(Project project) {
        if (project == null) return null;
        ProjectSummaryDTO dto = new ProjectSummaryDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        // The following fields may need to be filled from a join entity (e.g. ProjectTeamMember)
        // dto.setRole(...);
        // dto.setAllocation(...);
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        return dto;
    }

    // If you need to convert back (not common for summary), here is a stub:
    public Project toEntity(ProjectSummaryDTO dto) {
        if (dto == null) return null;
        Project project = new Project();
        project.setId(dto.getId());
        project.setName(dto.getName());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        // Allocation and role are usually part of a join entity (ProjectTeamMember), so not mapped here.
        return project;
    }
}