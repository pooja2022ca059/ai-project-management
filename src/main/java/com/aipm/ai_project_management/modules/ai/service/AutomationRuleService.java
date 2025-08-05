package com.aipm.ai_project_management.modules.ai.service;

import com.aipm.ai_project_management.modules.ai.dto.AutomationRuleDTO;
import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AutomationRuleService {
    
    /**
     * Create a new automation rule
     */
    AutomationRuleDTO createRule(AutomationRuleDTO ruleDTO);
    
    /**
     * Update an existing automation rule
     */
    AutomationRuleDTO updateRule(Long ruleId, AutomationRuleDTO ruleDTO);
    
    /**
     * Get automation rule by ID
     */
    AutomationRuleDTO getRuleById(Long ruleId);
    
    /**
     * Delete an automation rule
     */
    void deleteRule(Long ruleId);
    
    /**
     * Get all active rules
     */
    List<AutomationRuleDTO> getActiveRules();
    
    /**
     * Get rules by type
     */
    List<AutomationRuleDTO> getRulesByType(AutomationRuleEntity.RuleType type);
    
    /**
     * Get rules for a specific project
     */
    List<AutomationRuleDTO> getProjectRules(Long projectId);
    
    /**
     * Get global rules (not project-specific)
     */
    List<AutomationRuleDTO> getGlobalRules();
    
    /**
     * Get rules created by a user
     */
    Page<AutomationRuleDTO> getUserRules(Long userId, Pageable pageable);
    
    /**
     * Execute applicable automation rules for an event
     */
    void executeRulesForEvent(String eventType, Object eventData);
    
    /**
     * Toggle rule active status
     */
    AutomationRuleDTO toggleRuleStatus(Long ruleId);
    
    /**
     * Get rule execution statistics
     */
    RuleExecutionStats getRuleExecutionStats(Long ruleId);
    
    /**
     * Get all applicable rules for a project
     */
    List<AutomationRuleDTO> getApplicableRulesForProject(Long projectId);
    
    /**
     * Test rule execution (dry run)
     */
    RuleTestResult testRule(Long ruleId, Object testData);
    
    /**
     * Inner class for rule execution statistics
     */
    class RuleExecutionStats {
        private Long executionCount;
        private Long successCount;
        private Long failureCount;
        private Double successRate;
        
        public RuleExecutionStats(Long executionCount, Long successCount, Long failureCount) {
            this.executionCount = executionCount;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.successRate = executionCount > 0 ? (double) successCount / executionCount * 100 : 0.0;
        }
        
        // Getters
        public Long getExecutionCount() { return executionCount; }
        public Long getSuccessCount() { return successCount; }
        public Long getFailureCount() { return failureCount; }
        public Double getSuccessRate() { return successRate; }
    }
    
    /**
     * Inner class for rule test results
     */
    class RuleTestResult {
        private boolean success;
        private String message;
        private Object resultData;
        
        public RuleTestResult(boolean success, String message, Object resultData) {
            this.success = success;
            this.message = message;
            this.resultData = resultData;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getResultData() { return resultData; }
    }
}