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
    
    Page<AiInsightEntity> findByStatusOrderByCreatedAtDesc(
        AiInsightEntity.InsightStatus status, Pageable pageable);
    
    Page<AiInsightEntity> findByTypeAndStatusOrderByCreatedAtDesc(
        AiInsightEntity.InsightType type, AiInsightEntity.InsightStatus status, Pageable pageable);
    
    Page<AiInsightEntity> findByPriorityAndStatusOrderByCreatedAtDesc(
        AiInsightEntity.InsightPriority priority, AiInsightEntity.InsightStatus status, Pageable pageable);
    
    List<AiInsightEntity> findByRelatedEntityTypeAndRelatedEntityIdAndStatus(
        String entityType, Long entityId, AiInsightEntity.InsightStatus status);
    
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.expiresAt IS NOT NULL AND ai.expiresAt < :currentTime AND ai.status = 'ACTIVE'")
    List<AiInsightEntity> findExpiredInsights(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(ai) FROM AiInsightEntity ai WHERE ai.status = :status")
    long countByStatus(@Param("status") AiInsightEntity.InsightStatus status);
    
    @Query("SELECT ai FROM AiInsightEntity ai WHERE ai.status = 'ACTIVE' AND ai.priority IN ('HIGH', 'CRITICAL') ORDER BY ai.priority DESC, ai.createdAt DESC")
    List<AiInsightEntity> findHighPriorityActiveInsights();
    
    @Query("SELECT COUNT(ai) FROM AiInsightEntity ai WHERE ai.relatedEntityType = :entityType AND ai.relatedEntityId = :entityId AND ai.status = 'ACTIVE'")
    long countActiveInsightsByEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId);
}