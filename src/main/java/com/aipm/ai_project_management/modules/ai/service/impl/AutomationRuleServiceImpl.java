package com.aipm.ai_project_management.modules.ai.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.ai.dto.AutomationRuleDTO;
import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import com.aipm.ai_project_management.modules.ai.repository.AutomationRuleRepository;
import com.aipm.ai_project_management.modules.ai.service.AutomationRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AutomationRuleServiceImpl implements AutomationRuleService {
    
    private static final Logger logger = LoggerFactory.getLogger(AutomationRuleServiceImpl.class);
    
    @Autowired
    private AutomationRuleRepository automationRuleRepository;
    
    @Override
    public AutomationRuleDTO createRule(AutomationRuleDTO ruleDTO) {
        logger.info("Creating automation rule: {}", ruleDTO.getName());
        
        // Check if rule name already exists for this user
        if (automationRuleRepository.existsByNameAndRuleCreatedBy(ruleDTO.getName(), ruleDTO.getRuleCreatedBy())) {
            throw new IllegalArgumentException("Automation rule with name '" + ruleDTO.getName() + "' already exists");
        }
        
        AutomationRuleEntity rule = convertToEntity(ruleDTO);
        rule.setIsActive(true);
        rule.setExecutionCount(0L);
        rule.setSuccessCount(0L);
        rule.setFailureCount(0L);
        
        AutomationRuleEntity savedRule = automationRuleRepository.save(rule);
        logger.info("Created automation rule with ID: {}", savedRule.getId());
        
        return convertToDTO(savedRule);
    }
    
    @Override
    public AutomationRuleDTO updateRule(Long ruleId, AutomationRuleDTO ruleDTO) {
        logger.info("Updating automation rule with ID: {}", ruleId);
        
        AutomationRuleEntity existingRule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        // Check if new name conflicts with existing rules (excluding current rule)
        if (!existingRule.getName().equals(ruleDTO.getName()) && 
            automationRuleRepository.existsByNameAndRuleCreatedBy(ruleDTO.getName(), ruleDTO.getRuleCreatedBy())) {
            throw new IllegalArgumentException("Automation rule with name '" + ruleDTO.getName() + "' already exists");
        }
        
        // Update fields
        existingRule.setName(ruleDTO.getName());
        existingRule.setDescription(ruleDTO.getDescription());
        existingRule.setType(ruleDTO.getType());
        existingRule.setTriggerConditions(ruleDTO.getTriggerConditions());
        existingRule.setActions(ruleDTO.getActions());
        existingRule.setPriority(ruleDTO.getPriority());
        existingRule.setProjectId(ruleDTO.getProjectId());
        
        AutomationRuleEntity updatedRule = automationRuleRepository.save(existingRule);
        logger.info("Updated automation rule with ID: {}", ruleId);
        
        return convertToDTO(updatedRule);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AutomationRuleDTO getRuleById(Long ruleId) {
        logger.info("Fetching automation rule with ID: {}", ruleId);
        
        AutomationRuleEntity rule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        return convertToDTO(rule);
    }
    
    @Override
    public void deleteRule(Long ruleId) {
        logger.info("Deleting automation rule with ID: {}", ruleId);
        
        AutomationRuleEntity rule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        automationRuleRepository.delete(rule);
        logger.info("Deleted automation rule with ID: {}", ruleId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getActiveRules() {
        logger.info("Fetching all active automation rules");
        
        List<AutomationRuleEntity> rules = automationRuleRepository.findByIsActiveOrderByPriorityDescCreatedAtDesc(true);
        return rules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getRulesByType(AutomationRuleEntity.RuleType type) {
        logger.info("Fetching automation rules by type: {}", type);
        
        List<AutomationRuleEntity> rules = automationRuleRepository.findByTypeAndIsActiveOrderByPriorityDescCreatedAtDesc(type, true);
        return rules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getProjectRules(Long projectId) {
        logger.info("Fetching automation rules for project ID: {}", projectId);
        
        List<AutomationRuleEntity> rules = automationRuleRepository.findByProjectIdAndIsActiveOrderByPriorityDescCreatedAtDesc(projectId, true);
        return rules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getGlobalRules() {
        logger.info("Fetching global automation rules");
        
        List<AutomationRuleEntity> rules = automationRuleRepository.findByProjectIdIsNullAndIsActiveOrderByPriorityDescCreatedAtDesc(true);
        return rules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AutomationRuleDTO> getUserRules(Long userId, Pageable pageable) {
        logger.info("Fetching automation rules for user ID: {}", userId);
        
        Page<AutomationRuleEntity> rules = automationRuleRepository.findByRuleCreatedByOrderByCreatedAtDesc(userId, pageable);
        return rules.map(this::convertToDTO);
    }
    
    @Override
    public void executeRulesForEvent(String eventType, Object eventData) {
        logger.info("Executing automation rules for event type: {}", eventType);
        
        // This is a simplified implementation
        // In a real system, this would analyze the event and execute matching rules
        List<AutomationRuleEntity> activeRules = automationRuleRepository.findByIsActiveOrderByPriorityDescCreatedAtDesc(true);
        
        for (AutomationRuleEntity rule : activeRules) {
            try {
                if (shouldExecuteRule(rule, eventType, eventData)) {
                    executeRule(rule, eventData);
                    rule.incrementExecutionCount();
                    rule.recordSuccess();
                    automationRuleRepository.save(rule);
                }
            } catch (Exception e) {
                logger.error("Error executing rule {}: {}", rule.getId(), e.getMessage());
                rule.incrementExecutionCount();
                rule.recordFailure(e.getMessage());
                automationRuleRepository.save(rule);
            }
        }
    }
    
    @Override
    public AutomationRuleDTO toggleRuleStatus(Long ruleId) {
        logger.info("Toggling status for automation rule with ID: {}", ruleId);
        
        AutomationRuleEntity rule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        rule.setIsActive(!rule.getIsActive());
        AutomationRuleEntity updatedRule = automationRuleRepository.save(rule);
        
        logger.info("Toggled rule {} status to: {}", ruleId, updatedRule.getIsActive());
        return convertToDTO(updatedRule);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RuleExecutionStats getRuleExecutionStats(Long ruleId) {
        logger.info("Fetching execution stats for automation rule with ID: {}", ruleId);
        
        AutomationRuleEntity rule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        return new RuleExecutionStats(rule.getExecutionCount(), rule.getSuccessCount(), rule.getFailureCount());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getApplicableRulesForProject(Long projectId) {
        logger.info("Fetching applicable automation rules for project ID: {}", projectId);
        
        List<AutomationRuleEntity> rules = automationRuleRepository.findApplicableRulesForProject(projectId);
        return rules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public RuleTestResult testRule(Long ruleId, Object testData) {
        logger.info("Testing automation rule with ID: {}", ruleId);
        
        AutomationRuleEntity rule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        try {
            // Simulate rule execution without actually performing actions
            boolean wouldExecute = shouldExecuteRule(rule, "TEST_EVENT", testData);
            String message = wouldExecute ? "Rule would be executed successfully" : "Rule conditions not met";
            
            return new RuleTestResult(wouldExecute, message, testData);
        } catch (Exception e) {
            logger.error("Error testing rule {}: {}", ruleId, e.getMessage());
            return new RuleTestResult(false, "Rule test failed: " + e.getMessage(), null);
        }
    }
    
    // Private helper methods
    
    private boolean shouldExecuteRule(AutomationRuleEntity rule, String eventType, Object eventData) {
        // Simplified logic - in real implementation, this would parse trigger conditions
        // and evaluate them against the event data
        
        if (!rule.getIsActive()) {
            return false;
        }
        
        // Parse trigger conditions (simplified)
        String conditions = rule.getTriggerConditions();
        if (conditions == null || conditions.isEmpty()) {
            return false;
        }
        
        // For demo purposes, simulate some conditions
        switch (rule.getType()) {
            case TASK_AUTO_ASSIGNMENT:
                return eventType.equals("TASK_CREATED");
            case STATUS_CHANGE_TRIGGER:
                return eventType.equals("STATUS_CHANGED");
            case DEADLINE_NOTIFICATION:
                return eventType.equals("DEADLINE_APPROACHING");
            default:
                return false;
        }
    }
    
    private void executeRule(AutomationRuleEntity rule, Object eventData) {
        // Simplified rule execution - in real implementation, this would parse
        // and execute the actions defined in the rule
        
        logger.info("Executing rule: {} (Type: {})", rule.getName(), rule.getType());
        
        String actions = rule.getActions();
        if (actions == null || actions.isEmpty()) {
            logger.warn("No actions defined for rule: {}", rule.getId());
            return;
        }
        
        // Simulate action execution based on rule type
        switch (rule.getType()) {
            case TASK_AUTO_ASSIGNMENT:
                logger.info("Simulating task auto-assignment");
                break;
            case STATUS_CHANGE_TRIGGER:
                logger.info("Simulating status change trigger");
                break;
            case DEADLINE_NOTIFICATION:
                logger.info("Simulating deadline notification");
                break;
            default:
                logger.info("Executing custom rule action");
        }
    }
    
    private AutomationRuleDTO convertToDTO(AutomationRuleEntity entity) {
        AutomationRuleDTO dto = new AutomationRuleDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType());
        dto.setTriggerConditions(entity.getTriggerConditions());
        dto.setActions(entity.getActions());
        dto.setIsActive(entity.getIsActive());
        dto.setRuleCreatedBy(entity.getRuleCreatedBy());
        dto.setProjectId(entity.getProjectId());
        dto.setPriority(entity.getPriority());
        dto.setExecutionCount(entity.getExecutionCount());
        dto.setLastExecutedAt(entity.getLastExecutedAt());
        dto.setSuccessCount(entity.getSuccessCount());
        dto.setFailureCount(entity.getFailureCount());
        dto.setLastError(entity.getLastError());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
    
    private AutomationRuleEntity convertToEntity(AutomationRuleDTO dto) {
        AutomationRuleEntity entity = new AutomationRuleEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setTriggerConditions(dto.getTriggerConditions());
        entity.setActions(dto.getActions());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        entity.setRuleCreatedBy(dto.getRuleCreatedBy());
        entity.setProjectId(dto.getProjectId());
        entity.setPriority(dto.getPriority() != null ? dto.getPriority() : AutomationRuleEntity.RulePriority.MEDIUM);
        return entity;
    }
}