package com.aipm.ai_project_management.modules.projects.repository;

import com.aipm.ai_project_management.modules.projects.entity.ProjectMilestone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, Long> {
    
    // Find milestones by project
    Page<ProjectMilestone> findByProjectId(Long projectId, Pageable pageable);
    
    // Find completed milestones by project
    List<ProjectMilestone> findByProjectIdAndIsCompleted(Long projectId, Boolean isCompleted);
    
    // Find milestones due soon
    @Query("SELECT m FROM ProjectMilestone m WHERE m.dueDate BETWEEN :startDate AND :endDate AND m.isCompleted = false")
    List<ProjectMilestone> findMilestonesDueSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find overdue milestones
    @Query("SELECT m FROM ProjectMilestone m WHERE m.dueDate < :currentDate AND m.isCompleted = false")
    List<ProjectMilestone> findOverdueMilestones(@Param("currentDate") LocalDate currentDate);
    
    // Count milestones by project and completion status
    long countByProjectIdAndIsCompleted(Long projectId, Boolean isCompleted);
    
    // Find milestones by project and date range
    @Query("SELECT m FROM ProjectMilestone m WHERE m.project.id = :projectId AND m.dueDate BETWEEN :startDate AND :endDate")
    List<ProjectMilestone> findByProjectAndDateRange(@Param("projectId") Long projectId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}