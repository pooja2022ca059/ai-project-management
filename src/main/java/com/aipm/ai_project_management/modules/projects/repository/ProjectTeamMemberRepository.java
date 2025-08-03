package com.aipm.ai_project_management.modules.projects.repository;

import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.modules.projects.entity.ProjectTeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectTeamMemberRepository extends JpaRepository<ProjectTeamMember, Long> {

    // Find team members by project
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId")
    Page<ProjectTeamMember> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    // Find active team members by project (leftAt IS NULL means active)
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.leftAt IS NULL")
    List<ProjectTeamMember> findActiveByProjectId(@Param("projectId") Long projectId);

    // Find team members by user
    Page<ProjectTeamMember> findByUserId(Long userId, Pageable pageable);

    // Find active team members by user
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.userId = :userId AND ptm.leftAt IS NULL")
    List<ProjectTeamMember> findActiveByUserId(@Param("userId") Long userId);

    // Find team member by project and user
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.userId = :userId")
    Optional<ProjectTeamMember> findByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    // Find team members by role
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.role = :role")
    List<ProjectTeamMember> findByProjectIdAndRole(@Param("projectId") Long projectId, @Param("role") UserRole role);

    // Check if user is active member of project
    @Query("SELECT COUNT(ptm) > 0 FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.userId = :userId AND ptm.leftAt IS NULL")
    boolean existsActiveByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    // Count active team members by project
    @Query("SELECT COUNT(ptm) FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.leftAt IS NULL")
    long countActiveByProjectId(@Param("projectId") Long projectId);

    // Find team members assigned by user
    List<ProjectTeamMember> findByAssignedBy(Long assignedBy);

    // Get user's project roles (only active)
    @Query("SELECT ptm.role FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.userId = :userId AND ptm.leftAt IS NULL")
    List<UserRole> findUserRolesInProject(@Param("projectId") Long projectId, @Param("userId") Long userId);

    // Additional helper: all active team members by project
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.project.id = :projectId AND ptm.leftAt IS NULL")
    List<ProjectTeamMember> findActiveTeamMembersByProjectId(@Param("projectId") Long projectId);

    // Additional helper: all active projects for user
    @Query("SELECT ptm FROM ProjectTeamMember ptm WHERE ptm.userId = :userId AND ptm.leftAt IS NULL")
    List<ProjectTeamMember> findActiveProjectsByUserId(@Param("userId") Long userId);
}