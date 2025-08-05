package com.aipm.ai_project_management.modules.ai.repository;

import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AutomationRuleRepository extends JpaRepository<AutomationRuleEntity, Long> {

    // Find active rules
    List<AutomationRuleEntity> findByIsActiveTrue();
    
    // Find rules by type
    List<AutomationRuleEntity> findByTypeAndIsActiveTrue(AutomationRuleEntity.RuleType type);
    
    // Find rules by priority
    List<AutomationRuleEntity> findByPriorityAndIsActiveTrue(AutomationRuleEntity.RulePriority priority);
    
    // Find global rules (project-wide)
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.projectId IS NULL AND ar.isActive = true")
    List<AutomationRuleEntity> findGlobalActiveRules();
    
    // Find project-specific rules
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.projectId = :projectId AND ar.isActive = true")
    List<AutomationRuleEntity> findProjectActiveRules(@Param("projectId") Long projectId);
    
    // Find rules by creator
    List<AutomationRuleEntity> findByRuleCreatedByAndIsActiveTrue(Long createdBy);
    
    // Find rules created recently
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.createdAt >= :since ORDER BY ar.createdAt DESC")
    List<AutomationRuleEntity> findRecentRules(@Param("since") LocalDateTime since);
    
    // Find rules that have never been executed
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.lastExecutedAt IS NULL AND ar.isActive = true")
    List<AutomationRuleEntity> findNeverExecutedRules();
    
    // Find frequently failing rules
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.executionCount > 0 AND (ar.failureCount * 1.0 / ar.executionCount) > :failureThreshold")
    List<AutomationRuleEntity> findFrequentlyFailingRules(@Param("failureThreshold") Double failureThreshold);
    
    // Find high-performing rules
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.executionCount >= :minExecutions AND (ar.successCount * 1.0 / ar.executionCount) >= :successThreshold ORDER BY ar.successCount DESC")
    List<AutomationRuleEntity> findHighPerformingRules(@Param("minExecutions") Long minExecutions, @Param("successThreshold") Double successThreshold);
    
    // Find rules with pagination
    Page<AutomationRuleEntity> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Count rules by type
    long countByTypeAndIsActiveTrue(AutomationRuleEntity.RuleType type);
    
    // Check if rule name exists for user
    boolean existsByNameAndRuleCreatedBy(String name, Long createdBy);
    
    // Find rules that need maintenance (high failure rate)
    @Query("SELECT ar FROM AutomationRuleEntity ar WHERE ar.isActive = true AND ar.executionCount > 10 AND (ar.failureCount * 1.0 / ar.executionCount) > 0.5")
    List<AutomationRuleEntity> findRulesNeedingMaintenance();
}