package com.aipm.ai_project_management.modules.ai.service;

import com.aipm.ai_project_management.modules.ai.dto.AiInsightDTO;
import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AiService {
    
    /**
     * Generate AI insights for a project
     */
    List<AiInsightDTO> generateProjectInsights(Long projectId);
    
    /**
     * Generate AI insights for a task
     */
    List<AiInsightDTO> generateTaskInsights(Long taskId);
    
    /**
     * Generate AI insights for team performance
     */
    List<AiInsightDTO> generateTeamInsights(Long teamId);
    
    /**
     * Get all active insights
     */
    Page<AiInsightDTO> getActiveInsights(Pageable pageable);
    
    /**
     * Get insights by type
     */
    Page<AiInsightDTO> getInsightsByType(AiInsightEntity.InsightType type, Pageable pageable);
    
    /**
     * Get high priority insights
     */
    List<AiInsightDTO> getHighPriorityInsights();
    
    /**
     * Get insights for specific entity
     */
    List<AiInsightDTO> getInsightsForEntity(String entityType, Long entityId);
    
    /**
     * Mark insight as taken action
     */
    AiInsightDTO markInsightActionTaken(Long insightId, Long userId);
    
    /**
     * Dismiss an insight
     */
    void dismissInsight(Long insightId);
    
    /**
     * Clean up expired insights
     */
    void cleanupExpiredInsights();
    
    /**
     * Get insight statistics
     */
    AiInsightStats getInsightStatistics();
    
    /**
     * Inner class for statistics
     */
    class AiInsightStats {
        private long totalInsights;
        private long activeInsights;
        private long dismissedInsights;
        private long implementedInsights;
        
        public AiInsightStats(long totalInsights, long activeInsights, long dismissedInsights, long implementedInsights) {
            this.totalInsights = totalInsights;
            this.activeInsights = activeInsights;
            this.dismissedInsights = dismissedInsights;
            this.implementedInsights = implementedInsights;
        }
        
        // Getters
        public long getTotalInsights() { return totalInsights; }
        public long getActiveInsights() { return activeInsights; }
        public long getDismissedInsights() { return dismissedInsights; }
        public long getImplementedInsights() { return implementedInsights; }
    }
}