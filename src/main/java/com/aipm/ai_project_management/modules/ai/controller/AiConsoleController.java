package com.aipm.ai_project_management.modules.ai.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.common.response.PageResponse;
import com.aipm.ai_project_management.modules.ai.dto.AiInsightDTO;
import com.aipm.ai_project_management.modules.ai.dto.AutomationRuleDTO;
import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import com.aipm.ai_project_management.modules.ai.entity.AutomationRuleEntity;
import com.aipm.ai_project_management.modules.ai.service.AiService;
import com.aipm.ai_project_management.modules.ai.service.AutomationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Console", description = "AI insights and automation management")
public class AiConsoleController {
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private AutomationRuleService automationRuleService;
    
    // ==================== AI INSIGHTS ENDPOINTS ====================
    
    @PostMapping("/insights/project/{projectId}/generate")
    @Operation(summary = "Generate AI insights for a project")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AiInsightDTO>>> generateProjectInsights(
            @Parameter(description = "Project ID") @PathVariable Long projectId) {
        
        List<AiInsightDTO> insights = aiService.generateProjectInsights(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project insights generated successfully", insights));
    }
    
    @PostMapping("/insights/task/{taskId}/generate")
    @Operation(summary = "Generate AI insights for a task")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AiInsightDTO>>> generateTaskInsights(
            @Parameter(description = "Task ID") @PathVariable Long taskId) {
        
        List<AiInsightDTO> insights = aiService.generateTaskInsights(taskId);
        return ResponseEntity.ok(ApiResponse.success("Task insights generated successfully", insights));
    }
    
    @PostMapping("/insights/team/{teamId}/generate")
    @Operation(summary = "Generate AI insights for a team")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AiInsightDTO>>> generateTeamInsights(
            @Parameter(description = "Team ID") @PathVariable Long teamId) {
        
        List<AiInsightDTO> insights = aiService.generateTeamInsights(teamId);
        return ResponseEntity.ok(ApiResponse.success("Team insights generated successfully", insights));
    }
    
    @GetMapping("/insights")
    @Operation(summary = "Get active AI insights")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<AiInsightDTO>>> getActiveInsights(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AiInsightDTO> insights = aiService.getActiveInsights(pageable);
        
        PageResponse<AiInsightDTO> pageResponse = new PageResponse<>(
            insights.getContent(),
            insights.getNumber(),
            insights.getSize(),
            insights.getTotalElements(),
            insights.getTotalPages(),
            insights.isFirst(),
            insights.isLast()
        );
        
        return ResponseEntity.ok(ApiResponse.success("Active insights retrieved successfully", pageResponse));
    }
    
    @GetMapping("/insights/high-priority")
    @Operation(summary = "Get high priority insights")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AiInsightDTO>>> getHighPriorityInsights() {
        List<AiInsightDTO> insights = aiService.getHighPriorityInsights();
        return ResponseEntity.ok(ApiResponse.success("High priority insights retrieved successfully", insights));
    }
    
    @PutMapping("/insights/{insightId}/action-taken")
    @Operation(summary = "Mark insight as action taken")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AiInsightDTO>> markInsightActionTaken(
            @Parameter(description = "Insight ID") @PathVariable Long insightId,
            @Parameter(description = "User ID") @RequestParam Long userId) {
        
        AiInsightDTO insight = aiService.markInsightActionTaken(insightId, userId);
        return ResponseEntity.ok(ApiResponse.success("Insight marked as action taken", insight));
    }
    
    @PutMapping("/insights/{insightId}/dismiss")
    @Operation(summary = "Dismiss an insight")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> dismissInsight(
            @Parameter(description = "Insight ID") @PathVariable Long insightId) {
        
        aiService.dismissInsight(insightId);
        return ResponseEntity.ok(ApiResponse.success("Insight dismissed successfully", null));
    }
    
    // ==================== AUTOMATION RULES ENDPOINTS ====================
    
    @PostMapping("/automation/rules")
    @Operation(summary = "Create automation rule")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AutomationRuleDTO>> createAutomationRule(
            @Valid @RequestBody AutomationRuleDTO ruleDTO) {
        
        AutomationRuleDTO createdRule = automationRuleService.createRule(ruleDTO);
        return ResponseEntity.ok(ApiResponse.success("Automation rule created successfully", createdRule));
    }
    
    @GetMapping("/automation/rules")
    @Operation(summary = "Get active automation rules")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AutomationRuleDTO>>> getActiveAutomationRules() {
        List<AutomationRuleDTO> rules = automationRuleService.getActiveRules();
        return ResponseEntity.ok(ApiResponse.success("Active automation rules retrieved successfully", rules));
    }
    
    @PutMapping("/automation/rules/{ruleId}/toggle")
    @Operation(summary = "Toggle automation rule status")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AutomationRuleDTO>> toggleAutomationRuleStatus(
            @Parameter(description = "Rule ID") @PathVariable Long ruleId) {
        
        AutomationRuleDTO rule = automationRuleService.toggleRuleStatus(ruleId);
        return ResponseEntity.ok(ApiResponse.success("Automation rule status toggled successfully", rule));
    }
}