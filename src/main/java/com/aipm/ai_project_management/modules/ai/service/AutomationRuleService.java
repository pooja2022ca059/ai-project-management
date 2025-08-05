package com.aipm.ai_project_management.modules.ai.service;

import com.aipm.ai_project_management.modules.ai.dto.AutomationRuleDTO;
import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AutomationRuleService {
    
    // Create and manage rules
    AutomationRuleDTO createRule(AutomationRuleDTO ruleDTO);
    AutomationRuleDTO updateRule(Long id, AutomationRuleDTO ruleDTO);
    void deleteRule(Long id);
    AutomationRuleDTO getRuleById(Long id);
    
    // Find rules
    List<AutomationRuleDTO> getAllActiveRules();
    List<AutomationRuleDTO> getRulesByType(AutomationRuleEntity.RuleType type);
    List<AutomationRuleDTO> getRulesByCreator(Long createdBy);
    List<AutomationRuleDTO> getGlobalRules();
    List<AutomationRuleDTO> getProjectRules(Long projectId);
    
    // Pagination support
    Page<AutomationRuleDTO> getActiveRules(Pageable pageable);
    
    // Rule execution
    void executeRule(Long ruleId);
    void executeRulesForProject(Long projectId);
    void executeGlobalRules();
    boolean testRule(Long ruleId, String testConditions);
    
    // Rule management
    AutomationRuleDTO toggleRuleStatus(Long id);
    void resetRuleStatistics(Long id);
    
    // Analytics and monitoring
    List<AutomationRuleDTO> getHighPerformingRules();
    List<AutomationRuleDTO> getFailingRules();
    List<AutomationRuleDTO> getRulesNeedingMaintenance();
    long getRuleCountByType(AutomationRuleEntity.RuleType type);
    
    // Rule validation
    boolean validateRuleConditions(String conditions);
    boolean validateRuleActions(String actions);
    boolean isRuleNameUnique(String name, Long createdBy);
    
    // Rule templates
    List<AutomationRuleDTO> getRuleTemplates();
    AutomationRuleDTO createRuleFromTemplate(String templateName, Long projectId, Long createdBy);
}