package com.aipm.ai_project_management.modules.ai.repository;

import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomationRuleRepository extends JpaRepository<AutomationRuleEntity, Long> {
    
    List<AutomationRuleEntity> findByIsActiveOrderByPriorityDescCreatedAtDesc(Boolean isActive);
    
    List<AutomationRuleEntity> findByTypeAndIsActiveOrderByPriorityDescCreatedAtDesc(
        AutomationRuleEntity.RuleType type, Boolean isActive);
    
    List<AutomationRuleEntity> findByProjectIdAndIsActiveOrderByPriorityDescCreatedAtDesc(
        Long projectId, Boolean isActive);
    
    List<AutomationRuleEntity> findByProjectIdIsNullAndIsActiveOrderByPriorityDescCreatedAtDesc(
        Boolean isActive);
    
    Page<AutomationRuleEntity> findByRuleCreatedByOrderByCreatedAtDesc(
        Long createdBy, Pageable pageable);
    
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.isActive = true AND (ar.projectId = :projectId OR ar.projectId IS NULL) ORDER BY ar.priority DESC, ar.createdAt DESC")
    List<AutomationRuleEntity> findApplicableRulesForProject(@Param("projectId") Long projectId);
    
    @Query("SELECT COUNT(ar) FROM AutomationRuleEntity ar WHERE ar.isActive = true")
    long countActiveRules();
    
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.type = :type AND ar.isActive = true AND (ar.projectId = :projectId OR ar.projectId IS NULL)")
    List<AutomationRuleEntity> findActiveRulesByTypeForProject(
        @Param("type") AutomationRuleEntity.RuleType type, @Param("projectId") Long projectId);
    
    boolean existsByNameAndRuleCreatedBy(String name, Long createdBy);
}