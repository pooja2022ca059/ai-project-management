package com.aipm.ai_project_management.modules.projects.repository;

import com.aipm.ai_project_management.common.enums.ProjectStatus;
import com.aipm.ai_project_management.modules.projects.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    // Find projects by status
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);
    
    // Find projects by client
    Page<Project> findByClientId(Long clientId, Pageable pageable);
    
    // Find projects by project manager - FIXED: projectManagerId → managerId
    Page<Project> findByManagerId(Long managerId, Pageable pageable);
    
    // Find projects created by user
    Page<Project> findByCreatedBy(Long createdBy, Pageable pageable);
    
    // Find projects by name containing (case insensitive)
    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Find active projects (not cancelled or completed)
    @Query("SELECT p FROM Project p WHERE p.status NOT IN ('COMPLETED', 'CANCELLED')")
    Page<Project> findActiveProjects(Pageable pageable);
    
    // Find projects ending soon (within given days)
    @Query("SELECT p FROM Project p WHERE p.endDate BETWEEN :startDate AND :endDate AND p.status = 'IN_PROGRESS'")
    List<Project> findProjectsEndingSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find overdue projects
    @Query("SELECT p FROM Project p WHERE p.endDate < :currentDate AND p.status IN ('PLANNING', 'IN_PROGRESS')")
    List<Project> findOverdueProjects(@Param("currentDate") LocalDate currentDate);
    
    // Count projects by status
    long countByStatus(ProjectStatus status);
    
    // Find projects with team member - FIXED: Added proper join condition
    @Query("SELECT DISTINCT p FROM Project p JOIN p.teamMembers tm WHERE tm.userId = :userId")
    Page<Project> findProjectsByTeamMember(@Param("userId") Long userId, Pageable pageable);
    
    // Get project st atistics - FIXED: progressPercentage → progress
    @Query("SELECT COUNT(p), SUM(p.budget), AVG(p.progress) FROM Project p WHERE p.status = :status")
    Object[] getProjectStatsByStatus(@Param("status") ProjectStatus status);
    
    // Find projects by date range
    @Query("SELECT p FROM Project p WHERE p.startDate >= :startDate AND p.endDate <= :endDate")
    Page<Project> findProjectsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    // Check if project exists by name (for validation)
    boolean existsByNameIgnoreCase(String name);
    
    // Check if project exists by name excluding current project (for update validation)
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    
    // Additional useful methods based on your schema
    
    // Find projects by manager (alternative method name for backward compatibility)
    @Query("SELECT p FROM Project p WHERE p.managerId = :managerId")
    Page<Project> findByProjectManagerId(@Param("managerId") Long managerId, Pageable pageable);
    
    // Find projects with spent amount greater than budget
    @Query("SELECT p FROM Project p WHERE p.spent > p.budget AND p.budget IS NOT NULL AND p.spent IS NOT NULL")
    List<Project> findProjectsOverBudget();
    
    // Find projects by priority
    @Query("SELECT p FROM Project p WHERE p.priority = :priority")
    Page<Project> findByPriority(@Param("priority") String priority, Pageable pageable);
    
    // Find projects with low health score (if you want to use AI health score)
    @Query("SELECT p FROM Project p WHERE p.aiHealthScore < :threshold AND p.aiHealthScore IS NOT NULL")
    List<Project> findProjectsWithLowHealthScore(@Param("threshold") Double threshold);
    
    // Find projects by risk level
    @Query("SELECT p FROM Project p WHERE p.aiRiskLevel = :riskLevel")
    List<Project> findProjectsByRiskLevel(@Param("riskLevel") String riskLevel);
}