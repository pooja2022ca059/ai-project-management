package com.aipm.ai_project_management.modules.ai.repository;

import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AiInsightRepository extends JpaRepository<AiInsightEntity, Long> {

    // Find insights by status
    List<AiInsightEntity> findByStatus(AiInsightEntity.InsightStatus status);
    
    // Find insights by type
    List<AiInsightEntity> findByType(AiInsightEntity.InsightType type);
    
    // Find insights by priority
    List<AiInsightEntity> findByPriority(AiInsightEntity.InsightPriority priority);
    
    // Find insights for a specific entity
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.relatedEntityType = :entityType AND ai.relatedEntityId = :entityId AND ai.status = 'ACTIVE'")
    List<AiInsightEntity> findActiveInsightsForEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId);
    
    // Find recent insights
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.createdAt >= :since ORDER BY ai.createdAt DESC")
    List<AiInsightEntity> findRecentInsights(@Param("since") LocalDateTime since);
    
    // Find expired insights
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.expiresAt <= :now AND ai.status = 'ACTIVE'")
    List<AiInsightEntity> findExpiredInsights(@Param("now") LocalDateTime now);
    
    // Find high priority active insights
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.status = 'ACTIVE' AND ai.priority IN ('HIGH', 'CRITICAL') ORDER BY ai.priority DESC, ai.createdAt DESC")
    List<AiInsightEntity> findHighPriorityActiveInsights();
    
    // Count insights by status
    long countByStatus(AiInsightEntity.InsightStatus status);
    
    // Count insights by type
    long countByType(AiInsightEntity.InsightType type);
    
    // Find insights with pagination
    Page<AiInsightEntity> findByStatusOrderByCreatedAtDesc(AiInsightEntity.InsightStatus status, Pageable pageable);
    
    // Find insights by confidence score range
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.confidenceScore >= :minConfidence AND ai.status = 'ACTIVE' ORDER BY ai.confidenceScore DESC")
    List<AiInsightEntity> findByConfidenceScore(@Param("minConfidence") Double minConfidence);
    
    // Find insights that need action
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.actionTaken = false AND ai.status = 'ACTIVE' AND ai.priority IN ('HIGH', 'CRITICAL')")
    List<AiInsightEntity> findInsightsNeedingAction();
}