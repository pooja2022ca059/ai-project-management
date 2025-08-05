package com.aipm.ai_project_management.modules.ai.controller;

import com.aipm.ai_project_management.modules.ai.dto.AutomationRuleDTO;
import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import com.aipm.ai_project_management.modules.ai.service.AutomationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/automation-rules")
@Tag(name = "Automation Rules", description = "AI-powered automation rules management")
public class AutomationRuleController {
    
    private static final Logger logger = LoggerFactory.getLogger(AutomationRuleController.class);
    
    @Autowired
    private AutomationRuleService automationRuleService;
    
    @PostMapping
    @Operation(summary = "Create a new automation rule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AutomationRuleDTO> createRule(
            @Valid @RequestBody AutomationRuleDTO ruleDTO,
            Authentication authentication) {
        
        // Extract user ID from authentication (simplified)
        Long userId = 1L; // In real implementation, extract from authentication
        ruleDTO.setRuleCreatedBy(userId);
        
        logger.info("Creating automation rule: {} by user: {}", ruleDTO.getName(), userId);
        AutomationRuleDTO createdRule = automationRuleService.createRule(ruleDTO);
        return new ResponseEntity<>(createdRule, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get automation rule by ID")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AutomationRuleDTO> getRuleById(@PathVariable Long id) {
        logger.info("Fetching automation rule with ID: {}", id);
        AutomationRuleDTO rule = automationRuleService.getRuleById(id);
        return ResponseEntity.ok(rule);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an automation rule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AutomationRuleDTO> updateRule(
            @PathVariable Long id,
            @Valid @RequestBody AutomationRuleDTO ruleDTO,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Updating automation rule {} by user: {}", id, userId);
        AutomationRuleDTO updatedRule = automationRuleService.updateRule(id, ruleDTO);
        return ResponseEntity.ok(updatedRule);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an automation rule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteRule(
            @PathVariable Long id,
            Authentication authentication) {
        
        logger.info("Deleting automation rule {} by user: {}", id, authentication.getName());
        automationRuleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    @Operation(summary = "Get all active automation rules with pagination")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<AutomationRuleDTO>> getActiveRules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("Fetching active automation rules with pagination");
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<AutomationRuleDTO> rules = automationRuleService.getActiveRules(pageable);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/my-rules")
    @Operation(summary = "Get current user's automation rules")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AutomationRuleDTO>> getMyRules(Authentication authentication) {
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Fetching automation rules for user: {}", userId);
        List<AutomationRuleDTO> rules = automationRuleService.getRulesByCreator(userId);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/by-type/{type}")
    @Operation(summary = "Get automation rules by type")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AutomationRuleDTO>> getRulesByType(
            @PathVariable AutomationRuleEntity.RuleType type) {
        
        logger.info("Fetching automation rules by type: {}", type);
        List<AutomationRuleDTO> rules = automationRuleService.getRulesByType(type);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/global")
    @Operation(summary = "Get global automation rules")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AutomationRuleDTO>> getGlobalRules() {
        logger.info("Fetching global automation rules");
        List<AutomationRuleDTO> rules = automationRuleService.getGlobalRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get automation rules for a project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AutomationRuleDTO>> getProjectRules(@PathVariable Long projectId) {
        logger.info("Fetching automation rules for project: {}", projectId);
        List<AutomationRuleDTO> rules = automationRuleService.getProjectRules(projectId);
        return ResponseEntity.ok(rules);
    }
    
    @PostMapping("/{id}/execute")
    @Operation(summary = "Execute an automation rule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> executeRule(
            @PathVariable Long id,
            Authentication authentication) {
        
        logger.info("Executing automation rule {} by user: {}", id, authentication.getName());
        
        try {
            automationRuleService.executeRule(id);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Rule executed successfully"));
        } catch (Exception e) {
            logger.error("Failed to execute rule {}: {}", id, e.getMessage());
            return ResponseEntity.ok(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    @PostMapping("/project/{projectId}/execute")
    @Operation(summary = "Execute all automation rules for a project")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> executeProjectRules(
            @PathVariable Long projectId,
            Authentication authentication) {
        
        logger.info("Executing automation rules for project {} by user: {}", projectId, authentication.getName());
        
        try {
            automationRuleService.executeRulesForProject(projectId);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Project rules executed successfully"));
        } catch (Exception e) {
            logger.error("Failed to execute project rules for {}: {}", projectId, e.getMessage());
            return ResponseEntity.ok(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    @PostMapping("/execute-global")
    @Operation(summary = "Execute all global automation rules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> executeGlobalRules(Authentication authentication) {
        logger.info("Executing global automation rules by admin: {}", authentication.getName());
        
        try {
            automationRuleService.executeGlobalRules();
            return ResponseEntity.ok(Map.of("status", "success", "message", "Global rules executed successfully"));
        } catch (Exception e) {
            logger.error("Failed to execute global rules: {}", e.getMessage());
            return ResponseEntity.ok(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/test")
    @Operation(summary = "Test an automation rule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> testRule(
            @PathVariable Long id,
            @RequestBody Map<String, String> testData,
            Authentication authentication) {
        
        logger.info("Testing automation rule {} by user: {}", id, authentication.getName());
        
        String testConditions = testData.get("testConditions");
        boolean result = automationRuleService.testRule(id, testConditions);
        
        return ResponseEntity.ok(Map.of(
            "ruleId", id,
            "testResult", result,
            "message", result ? "Rule test passed" : "Rule test failed"
        ));
    }
    
    @PostMapping("/{id}/toggle")
    @Operation(summary = "Toggle automation rule status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AutomationRuleDTO> toggleRuleStatus(
            @PathVariable Long id,
            Authentication authentication) {
        
        logger.info("Toggling automation rule {} status by user: {}", id, authentication.getName());
        AutomationRuleDTO rule = automationRuleService.toggleRuleStatus(id);
        return ResponseEntity.ok(rule);
    }
    
    @PostMapping("/{id}/reset-statistics")
    @Operation(summary = "Reset automation rule statistics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> resetRuleStatistics(
            @PathVariable Long id,
            Authentication authentication) {
        
        logger.info("Resetting statistics for automation rule {} by user: {}", id, authentication.getName());
        automationRuleService.resetRuleStatistics(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/analytics/high-performing")
    @Operation(summary = "Get high performing automation rules")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AutomationRuleDTO>> getHighPerformingRules() {
        logger.info("Fetching high performing automation rules");
        List<AutomationRuleDTO> rules = automationRuleService.getHighPerformingRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/analytics/failing")
    @Operation(summary = "Get failing automation rules")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AutomationRuleDTO>> getFailingRules() {
        logger.info("Fetching failing automation rules");
        List<AutomationRuleDTO> rules = automationRuleService.getFailingRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/analytics/maintenance")
    @Operation(summary = "Get automation rules needing maintenance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AutomationRuleDTO>> getRulesNeedingMaintenance() {
        logger.info("Fetching automation rules needing maintenance");
        List<AutomationRuleDTO> rules = automationRuleService.getRulesNeedingMaintenance();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/templates")
    @Operation(summary = "Get automation rule templates")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AutomationRuleDTO>> getRuleTemplates() {
        logger.info("Fetching automation rule templates");
        List<AutomationRuleDTO> templates = automationRuleService.getRuleTemplates();
        return ResponseEntity.ok(templates);
    }
    
    @PostMapping("/create-from-template")
    @Operation(summary = "Create rule from template")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AutomationRuleDTO> createRuleFromTemplate(
            @RequestParam String templateName,
            @RequestParam(required = false) Long projectId,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Creating rule from template {} for user: {}", templateName, userId);
        AutomationRuleDTO rule = automationRuleService.createRuleFromTemplate(templateName, projectId, userId);
        return new ResponseEntity<>(rule, HttpStatus.CREATED);
    }
    
    @GetMapping("/analytics/statistics")
    @Operation(summary = "Get automation rule statistics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getRuleStatistics() {
        logger.info("Fetching automation rule statistics");
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalRules", automationRuleService.getAllActiveRules().size());
        statistics.put("taskAutoAssignmentRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.TASK_AUTO_ASSIGNMENT));
        statistics.put("deadlineNotificationRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.DEADLINE_NOTIFICATION));
        statistics.put("statusChangeRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.STATUS_CHANGE_TRIGGER));
        statistics.put("budgetAlertRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.BUDGET_ALERT));
        statistics.put("resourceAllocationRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.RESOURCE_ALLOCATION));
        statistics.put("progressUpdateRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.PROGRESS_UPDATE));
        statistics.put("riskAssessmentRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.RISK_ASSESSMENT));
        statistics.put("workloadBalancingRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.WORKLOAD_BALANCING));
        statistics.put("qualityCheckRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.QUALITY_CHECK));
        statistics.put("communicationTriggerRules", automationRuleService.getRuleCountByType(AutomationRuleEntity.RuleType.COMMUNICATION_TRIGGER));
        
        return ResponseEntity.ok(statistics);
    }
}