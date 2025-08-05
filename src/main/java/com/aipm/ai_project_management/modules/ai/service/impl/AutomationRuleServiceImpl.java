package com.aipm.ai_project_management.modules.ai.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.ai.dto.AutomationRuleDTO;
import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import com.aipm.ai_project_management.modules.ai.repository.AutomationRuleRepository;
import com.aipm.ai_project_management.modules.ai.service.AutomationRuleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AutomationRuleServiceImpl implements AutomationRuleService {
    
    private static final Logger logger = LoggerFactory.getLogger(AutomationRuleServiceImpl.class);
    
    @Autowired
    private AutomationRuleRepository automationRuleRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public AutomationRuleDTO createRule(AutomationRuleDTO ruleDTO) {
        logger.info("Creating new automation rule: {}", ruleDTO.getName());
        
        // Validate rule name uniqueness
        if (automationRuleRepository.existsByNameAndRuleCreatedBy(ruleDTO.getName(), ruleDTO.getRuleCreatedBy())) {
            throw new IllegalArgumentException("Rule with name '" + ruleDTO.getName() + "' already exists for this user");
        }
        
        // Validate conditions and actions
        if (!validateRuleConditions(ruleDTO.getTriggerConditions())) {
            throw new IllegalArgumentException("Invalid trigger conditions format");
        }
        
        if (!validateRuleActions(ruleDTO.getActions())) {
            throw new IllegalArgumentException("Invalid actions format");
        }
        
        AutomationRuleEntity entity = mapDTOToEntity(ruleDTO);
        AutomationRuleEntity savedEntity = automationRuleRepository.save(entity);
        
        logger.info("Created automation rule with ID: {}", savedEntity.getId());
        return mapEntityToDTO(savedEntity);
    }
    
    @Override
    public AutomationRuleDTO updateRule(Long id, AutomationRuleDTO ruleDTO) {
        logger.info("Updating automation rule with ID: {}", id);
        
        AutomationRuleEntity entity = automationRuleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + id));
        
        // Check name uniqueness if name is being changed
        if (!entity.getName().equals(ruleDTO.getName()) && 
            automationRuleRepository.existsByNameAndRuleCreatedBy(ruleDTO.getName(), ruleDTO.getRuleCreatedBy())) {
            throw new IllegalArgumentException("Rule with name '" + ruleDTO.getName() + "' already exists for this user");
        }
        
        // Validate conditions and actions if provided
        if (ruleDTO.getTriggerConditions() != null && !validateRuleConditions(ruleDTO.getTriggerConditions())) {
            throw new IllegalArgumentException("Invalid trigger conditions format");
        }
        
        if (ruleDTO.getActions() != null && !validateRuleActions(ruleDTO.getActions())) {
            throw new IllegalArgumentException("Invalid actions format");
        }
        
        // Update fields
        entity.setName(ruleDTO.getName());
        entity.setDescription(ruleDTO.getDescription());
        entity.setTriggerConditions(ruleDTO.getTriggerConditions());
        entity.setActions(ruleDTO.getActions());
        entity.setPriority(ruleDTO.getPriority());
        entity.setProjectId(ruleDTO.getProjectId());
        
        AutomationRuleEntity updatedEntity = automationRuleRepository.save(entity);
        logger.info("Updated automation rule with ID: {}", id);
        return mapEntityToDTO(updatedEntity);
    }
    
    @Override
    public void deleteRule(Long id) {
        logger.info("Deleting automation rule with ID: {}", id);
        
        if (!automationRuleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Automation rule not found with id: " + id);
        }
        
        automationRuleRepository.deleteById(id);
        logger.info("Deleted automation rule with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AutomationRuleDTO getRuleById(Long id) {
        logger.info("Fetching automation rule with ID: {}", id);
        
        AutomationRuleEntity entity = automationRuleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + id));
        
        return mapEntityToDTO(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getAllActiveRules() {
        logger.info("Fetching all active automation rules");
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findByIsActiveTrue();
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getRulesByType(AutomationRuleEntity.RuleType type) {
        logger.info("Fetching rules by type: {}", type);
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findByTypeAndIsActiveTrue(type);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getRulesByCreator(Long createdBy) {
        logger.info("Fetching rules by creator: {}", createdBy);
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findByRuleCreatedByAndIsActiveTrue(createdBy);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getGlobalRules() {
        logger.info("Fetching global automation rules");
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findGlobalActiveRules();
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getProjectRules(Long projectId) {
        logger.info("Fetching automation rules for project: {}", projectId);
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findProjectActiveRules(projectId);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AutomationRuleDTO> getActiveRules(Pageable pageable) {
        logger.info("Fetching active rules with pagination");
        
        Page<AutomationRuleEntity> entities = automationRuleRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);
        return entities.map(this::mapEntityToDTO);
    }
    
    @Override
    public void executeRule(Long ruleId) {
        logger.info("Executing automation rule: {}", ruleId);
        
        AutomationRuleEntity rule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        if (!rule.getIsActive()) {
            throw new IllegalStateException("Cannot execute inactive rule");
        }
        
        try {
            // Increment execution count
            rule.incrementExecutionCount();
            
            // Simulate rule execution - in real implementation, this would:
            // 1. Parse trigger conditions
            // 2. Check if conditions are met
            // 3. Execute actions if conditions are satisfied
            boolean executionSuccess = simulateRuleExecution(rule);
            
            if (executionSuccess) {
                rule.recordSuccess();
                logger.info("Successfully executed rule: {}", ruleId);
            } else {
                rule.recordFailure("Conditions not met");
                logger.info("Rule execution skipped - conditions not met: {}", ruleId);
            }
            
        } catch (Exception e) {
            rule.recordFailure(e.getMessage());
            logger.error("Failed to execute rule: {}", ruleId, e);
            throw new RuntimeException("Rule execution failed: " + e.getMessage(), e);
        } finally {
            automationRuleRepository.save(rule);
        }
    }
    
    @Override
    public void executeRulesForProject(Long projectId) {
        logger.info("Executing all automation rules for project: {}", projectId);
        
        List<AutomationRuleEntity> projectRules = automationRuleRepository.findProjectActiveRules(projectId);
        List<AutomationRuleEntity> globalRules = automationRuleRepository.findGlobalActiveRules();
        
        // Execute project-specific rules
        for (AutomationRuleEntity rule : projectRules) {
            try {
                executeRule(rule.getId());
            } catch (Exception e) {
                logger.error("Failed to execute project rule {}: {}", rule.getId(), e.getMessage());
            }
        }
        
        // Execute global rules
        for (AutomationRuleEntity rule : globalRules) {
            try {
                executeRule(rule.getId());
            } catch (Exception e) {
                logger.error("Failed to execute global rule {}: {}", rule.getId(), e.getMessage());
            }
        }
    }
    
    @Override
    public void executeGlobalRules() {
        logger.info("Executing all global automation rules");
        
        List<AutomationRuleEntity> globalRules = automationRuleRepository.findGlobalActiveRules();
        
        for (AutomationRuleEntity rule : globalRules) {
            try {
                executeRule(rule.getId());
            } catch (Exception e) {
                logger.error("Failed to execute global rule {}: {}", rule.getId(), e.getMessage());
            }
        }
    }
    
    @Override
    public boolean testRule(Long ruleId, String testConditions) {
        logger.info("Testing automation rule: {}", ruleId);
        
        AutomationRuleEntity rule = automationRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + ruleId));
        
        // Simulate rule testing with provided conditions
        return validateRuleConditions(testConditions) && validateRuleActions(rule.getActions());
    }
    
    @Override
    public AutomationRuleDTO toggleRuleStatus(Long id) {
        logger.info("Toggling status for automation rule: {}", id);
        
        AutomationRuleEntity entity = automationRuleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + id));
        
        entity.setIsActive(!entity.getIsActive());
        AutomationRuleEntity updatedEntity = automationRuleRepository.save(entity);
        
        logger.info("Toggled rule {} status to: {}", id, updatedEntity.getIsActive());
        return mapEntityToDTO(updatedEntity);
    }
    
    @Override
    public void resetRuleStatistics(Long id) {
        logger.info("Resetting statistics for automation rule: {}", id);
        
        AutomationRuleEntity entity = automationRuleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Automation rule not found with id: " + id));
        
        entity.setExecutionCount(0L);
        entity.setSuccessCount(0L);
        entity.setFailureCount(0L);
        entity.setLastExecutedAt(null);
        entity.setLastError(null);
        
        automationRuleRepository.save(entity);
        logger.info("Reset statistics for rule: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getHighPerformingRules() {
        logger.info("Fetching high performing automation rules");
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findHighPerformingRules(10L, 0.9);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getFailingRules() {
        logger.info("Fetching failing automation rules");
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findFrequentlyFailingRules(0.5);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getRulesNeedingMaintenance() {
        logger.info("Fetching rules needing maintenance");
        
        List<AutomationRuleEntity> entities = automationRuleRepository.findRulesNeedingMaintenance();
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getRuleCountByType(AutomationRuleEntity.RuleType type) {
        return automationRuleRepository.countByTypeAndIsActiveTrue(type);
    }
    
    @Override
    public boolean validateRuleConditions(String conditions) {
        if (conditions == null || conditions.trim().isEmpty()) {
            return false;
        }
        
        try {
            JsonNode node = objectMapper.readTree(conditions);
            // Basic validation - check if it's valid JSON and has required fields
            return node.isObject() && node.has("conditions");
        } catch (Exception e) {
            logger.warn("Invalid rule conditions format: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean validateRuleActions(String actions) {
        if (actions == null || actions.trim().isEmpty()) {
            return false;
        }
        
        try {
            JsonNode node = objectMapper.readTree(actions);
            // Basic validation - check if it's valid JSON and has required fields
            return node.isObject() && node.has("actions");
        } catch (Exception e) {
            logger.warn("Invalid rule actions format: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isRuleNameUnique(String name, Long createdBy) {
        return !automationRuleRepository.existsByNameAndRuleCreatedBy(name, createdBy);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AutomationRuleDTO> getRuleTemplates() {
        logger.info("Fetching rule templates");
        
        // Return predefined rule templates
        return Arrays.asList(
            createTaskAutoAssignmentTemplate(),
            createDeadlineNotificationTemplate(),
            createStatusChangeTemplate(),
            createBudgetAlertTemplate()
        );
    }
    
    @Override
    public AutomationRuleDTO createRuleFromTemplate(String templateName, Long projectId, Long createdBy) {
        logger.info("Creating rule from template: {} for project: {}", templateName, projectId);
        
        AutomationRuleDTO template = null;
        
        switch (templateName.toUpperCase()) {
            case "TASK_AUTO_ASSIGNMENT":
                template = createTaskAutoAssignmentTemplate();
                break;
            case "DEADLINE_NOTIFICATION":
                template = createDeadlineNotificationTemplate();
                break;
            case "STATUS_CHANGE":
                template = createStatusChangeTemplate();
                break;
            case "BUDGET_ALERT":
                template = createBudgetAlertTemplate();
                break;
            default:
                throw new IllegalArgumentException("Unknown template: " + templateName);
        }
        
        // Customize template for specific project and user
        template.setProjectId(projectId);
        template.setRuleCreatedBy(createdBy);
        template.setName(template.getName() + " - " + System.currentTimeMillis()); // Make name unique
        
        return createRule(template);
    }
    
    // Private helper methods
    private boolean simulateRuleExecution(AutomationRuleEntity rule) {
        // This is a simplified simulation
        // In real implementation, this would parse conditions and check against actual data
        return Math.random() > 0.3; // 70% success rate simulation
    }
    
    private AutomationRuleDTO createTaskAutoAssignmentTemplate() {
        AutomationRuleDTO template = new AutomationRuleDTO();
        template.setName("Auto-assign tasks to available team members");
        template.setDescription("Automatically assigns new tasks to team members with lowest workload");
        template.setType(AutomationRuleEntity.RuleType.TASK_AUTO_ASSIGNMENT);
        template.setPriority(AutomationRuleEntity.RulePriority.MEDIUM);
        template.setTriggerConditions("{\"conditions\": [{\"field\": \"task.status\", \"operator\": \"equals\", \"value\": \"NEW\"}]}");
        template.setActions("{\"actions\": [{\"type\": \"assign_task\", \"strategy\": \"least_workload\"}]}");
        return template;
    }
    
    private AutomationRuleDTO createDeadlineNotificationTemplate() {
        AutomationRuleDTO template = new AutomationRuleDTO();
        template.setName("Deadline reminder notifications");
        template.setDescription("Send notifications when tasks are approaching deadline");
        template.setType(AutomationRuleEntity.RuleType.DEADLINE_NOTIFICATION);
        template.setPriority(AutomationRuleEntity.RulePriority.HIGH);
        template.setTriggerConditions("{\"conditions\": [{\"field\": \"task.deadline\", \"operator\": \"within_days\", \"value\": 2}]}");
        template.setActions("{\"actions\": [{\"type\": \"send_notification\", \"template\": \"deadline_reminder\"}]}");
        return template;
    }
    
    private AutomationRuleDTO createStatusChangeTemplate() {
        AutomationRuleDTO template = new AutomationRuleDTO();
        template.setName("Status change notifications");
        template.setDescription("Notify stakeholders when task status changes");
        template.setType(AutomationRuleEntity.RuleType.STATUS_CHANGE_TRIGGER);
        template.setPriority(AutomationRuleEntity.RulePriority.MEDIUM);
        template.setTriggerConditions("{\"conditions\": [{\"field\": \"task.status\", \"operator\": \"changed\", \"value\": \"any\"}]}");
        template.setActions("{\"actions\": [{\"type\": \"notify_stakeholders\", \"message\": \"Task status updated\"}]}");
        return template;
    }
    
    private AutomationRuleDTO createBudgetAlertTemplate() {
        AutomationRuleDTO template = new AutomationRuleDTO();
        template.setName("Budget alert notifications");
        template.setDescription("Alert when project budget exceeds threshold");
        template.setType(AutomationRuleEntity.RuleType.BUDGET_ALERT);
        template.setPriority(AutomationRuleEntity.RulePriority.CRITICAL);
        template.setTriggerConditions("{\"conditions\": [{\"field\": \"project.budget_used_percentage\", \"operator\": \"greater_than\", \"value\": 80}]}");
        template.setActions("{\"actions\": [{\"type\": \"send_alert\", \"recipients\": [\"project_manager\", \"finance_team\"]}]}");
        return template;
    }
    
    // Mapping methods
    private AutomationRuleEntity mapDTOToEntity(AutomationRuleDTO dto) {
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
    
    private AutomationRuleDTO mapEntityToDTO(AutomationRuleEntity entity) {
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
}