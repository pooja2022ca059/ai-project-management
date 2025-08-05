package com.aipm.ai_project_management.modules.ai.service;

import com.aipm.ai_project_management.modules.ai.dto.AiInsightDTO;
import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AiInsightService {
    
    // Create and manage insights
    AiInsightDTO createInsight(AiInsightDTO insightDTO);
    AiInsightDTO updateInsight(Long id, AiInsightDTO insightDTO);
    void deleteInsight(Long id);
    AiInsightDTO getInsightById(Long id);
    
    // Find insights
    List<AiInsightDTO> getAllActiveInsights();
    List<AiInsightDTO> getInsightsByType(AiInsightEntity.InsightType type);
    List<AiInsightDTO> getInsightsByPriority(AiInsightEntity.InsightPriority priority);
    List<AiInsightDTO> getInsightsForEntity(String entityType, Long entityId);
    List<AiInsightDTO> getHighPriorityInsights();
    List<AiInsightDTO> getInsightsNeedingAction();
    
    // Pagination support
    Page<AiInsightDTO> getInsightsByStatus(AiInsightEntity.InsightStatus status, Pageable pageable);
    
    // Insight actions
    AiInsightDTO markInsightActionTaken(Long id, Long actionTakenBy);
    AiInsightDTO dismissInsight(Long id, Long dismissedBy);
    void expireOldInsights();
    
    // Analytics
    long getInsightCount();
    long getInsightCountByType(AiInsightEntity.InsightType type);
    long getInsightCountByStatus(AiInsightEntity.InsightStatus status);
    
    // AI-powered insight generation
    List<AiInsightDTO> generateProjectInsights(Long projectId);
    List<AiInsightDTO> generateTaskInsights(Long taskId);
    List<AiInsightDTO> generateTeamInsights(Long teamId);
    AiInsightDTO generateBudgetInsight(Long projectId);
    AiInsightDTO generateDeadlineInsight(Long projectId);
    
    // Recent insights
    List<AiInsightDTO> getRecentInsights(int days);
}