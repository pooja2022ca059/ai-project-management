package com.aipm.ai_project_management.modules.ai.controller;

import com.aipm.ai_project_management.modules.ai.dto.AiInsightDTO;
import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import com.aipm.ai_project_management.modules.ai.service.AiInsightService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/insights")
@Tag(name = "AI Insights", description = "AI-powered insights and recommendations")
public class AiInsightController {
    
    private static final Logger logger = LoggerFactory.getLogger(AiInsightController.class);
    
    @Autowired
    private AiInsightService aiInsightService;
    
    @PostMapping
    @Operation(summary = "Create a new AI insight")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<AiInsightDTO> createInsight(
            @Valid @RequestBody AiInsightDTO insightDTO,
            Authentication authentication) {
        
        logger.info("Creating new insight by user: {}", authentication.getName());
        AiInsightDTO createdInsight = aiInsightService.createInsight(insightDTO);
        return new ResponseEntity<>(createdInsight, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get insight by ID")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiInsightDTO> getInsightById(
            @Parameter(description = "Insight ID") @PathVariable Long id) {
        
        logger.info("Fetching insight with ID: {}", id);
        AiInsightDTO insight = aiInsightService.getInsightById(id);
        return ResponseEntity.ok(insight);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an insight")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<AiInsightDTO> updateInsight(
            @PathVariable Long id,
            @Valid @RequestBody AiInsightDTO insightDTO,
            Authentication authentication) {
        
        logger.info("Updating insight {} by user: {}", id, authentication.getName());
        AiInsightDTO updatedInsight = aiInsightService.updateInsight(id, insightDTO);
        return ResponseEntity.ok(updatedInsight);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an insight")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Void> deleteInsight(
            @PathVariable Long id,
            Authentication authentication) {
        
        logger.info("Deleting insight {} by user: {}", id, authentication.getName());
        aiInsightService.deleteInsight(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    @Operation(summary = "Get all active insights with pagination")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<AiInsightDTO>> getInsights(
            @RequestParam(defaultValue = "ACTIVE") AiInsightEntity.InsightStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("Fetching insights with status: {}", status);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<AiInsightDTO> insights = aiInsightService.getInsightsByStatus(status, pageable);
        return ResponseEntity.ok(insights);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active insights")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> getActiveInsights() {
        logger.info("Fetching all active insights");
        List<AiInsightDTO> insights = aiInsightService.getAllActiveInsights();
        return ResponseEntity.ok(insights);
    }
    
    @GetMapping("/high-priority")
    @Operation(summary = "Get high priority insights")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> getHighPriorityInsights() {
        logger.info("Fetching high priority insights");
        List<AiInsightDTO> insights = aiInsightService.getHighPriorityInsights();
        return ResponseEntity.ok(insights);
    }
    
    @GetMapping("/need-action")
    @Operation(summary = "Get insights that need action")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> getInsightsNeedingAction() {
        logger.info("Fetching insights needing action");
        List<AiInsightDTO> insights = aiInsightService.getInsightsNeedingAction();
        return ResponseEntity.ok(insights);
    }
    
    @GetMapping("/by-type/{type}")
    @Operation(summary = "Get insights by type")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> getInsightsByType(
            @PathVariable AiInsightEntity.InsightType type) {
        
        logger.info("Fetching insights by type: {}", type);
        List<AiInsightDTO> insights = aiInsightService.getInsightsByType(type);
        return ResponseEntity.ok(insights);
    }
    
    @GetMapping("/by-priority/{priority}")
    @Operation(summary = "Get insights by priority")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> getInsightsByPriority(
            @PathVariable AiInsightEntity.InsightPriority priority) {
        
        logger.info("Fetching insights by priority: {}", priority);
        List<AiInsightDTO> insights = aiInsightService.getInsightsByPriority(priority);
        return ResponseEntity.ok(insights);
    }
    
    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Get insights for specific entity")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> getInsightsForEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        logger.info("Fetching insights for entity: {} with ID: {}", entityType, entityId);
        List<AiInsightDTO> insights = aiInsightService.getInsightsForEntity(entityType, entityId);
        return ResponseEntity.ok(insights);
    }
    
    @GetMapping("/recent")
    @Operation(summary = "Get recent insights")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> getRecentInsights(
            @RequestParam(defaultValue = "7") int days) {
        
        logger.info("Fetching insights from last {} days", days);
        List<AiInsightDTO> insights = aiInsightService.getRecentInsights(days);
        return ResponseEntity.ok(insights);
    }
    
    @PostMapping("/{id}/mark-action-taken")
    @Operation(summary = "Mark insight as action taken")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiInsightDTO> markInsightActionTaken(
            @PathVariable Long id,
            Authentication authentication) {
        
        // Extract user ID from authentication (simplified)
        Long userId = 1L; // In real implementation, extract from authentication
        
        logger.info("Marking insight {} as action taken by user: {}", id, userId);
        AiInsightDTO updatedInsight = aiInsightService.markInsightActionTaken(id, userId);
        return ResponseEntity.ok(updatedInsight);
    }
    
    @PostMapping("/{id}/dismiss")
    @Operation(summary = "Dismiss an insight")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiInsightDTO> dismissInsight(
            @PathVariable Long id,
            Authentication authentication) {
        
        // Extract user ID from authentication (simplified)
        Long userId = 1L; // In real implementation, extract from authentication
        
        logger.info("Dismissing insight {} by user: {}", id, userId);
        AiInsightDTO updatedInsight = aiInsightService.dismissInsight(id, userId);
        return ResponseEntity.ok(updatedInsight);
    }
    
    @PostMapping("/expire-old")
    @Operation(summary = "Expire old insights")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> expireOldInsights(Authentication authentication) {
        logger.info("Expiring old insights by admin: {}", authentication.getName());
        aiInsightService.expireOldInsights();
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/analytics")
    @Operation(summary = "Get insight analytics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getInsightAnalytics() {
        logger.info("Fetching insight analytics");
        
        Map<String, Object> analytics = Map.of(
            "totalInsights", aiInsightService.getInsightCount(),
            "activeInsights", aiInsightService.getInsightCountByStatus(AiInsightEntity.InsightStatus.ACTIVE),
            "dismissedInsights", aiInsightService.getInsightCountByStatus(AiInsightEntity.InsightStatus.DISMISSED),
            "implementedInsights", aiInsightService.getInsightCountByStatus(AiInsightEntity.InsightStatus.IMPLEMENTED),
            "projectRiskAlerts", aiInsightService.getInsightCountByType(AiInsightEntity.InsightType.PROJECT_RISK_ALERT),
            "taskRecommendations", aiInsightService.getInsightCountByType(AiInsightEntity.InsightType.TASK_RECOMMENDATION),
            "teamProductivity", aiInsightService.getInsightCountByType(AiInsightEntity.InsightType.TEAM_PRODUCTIVITY),
            "budgetForecasts", aiInsightService.getInsightCountByType(AiInsightEntity.InsightType.BUDGET_FORECAST)
        );
        
        return ResponseEntity.ok(analytics);
    }
    
    // AI-powered insight generation endpoints
    @PostMapping("/generate/project/{projectId}")
    @Operation(summary = "Generate insights for a project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> generateProjectInsights(
            @PathVariable Long projectId,
            Authentication authentication) {
        
        logger.info("Generating insights for project {} by user: {}", projectId, authentication.getName());
        List<AiInsightDTO> insights = aiInsightService.generateProjectInsights(projectId);
        return ResponseEntity.ok(insights);
    }
    
    @PostMapping("/generate/task/{taskId}")
    @Operation(summary = "Generate insights for a task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> generateTaskInsights(
            @PathVariable Long taskId,
            Authentication authentication) {
        
        logger.info("Generating insights for task {} by user: {}", taskId, authentication.getName());
        List<AiInsightDTO> insights = aiInsightService.generateTaskInsights(taskId);
        return ResponseEntity.ok(insights);
    }
    
    @PostMapping("/generate/team/{teamId}")
    @Operation(summary = "Generate insights for a team")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiInsightDTO>> generateTeamInsights(
            @PathVariable Long teamId,
            Authentication authentication) {
        
        logger.info("Generating insights for team {} by user: {}", teamId, authentication.getName());
        List<AiInsightDTO> insights = aiInsightService.generateTeamInsights(teamId);
        return ResponseEntity.ok(insights);
    }
    
    @PostMapping("/generate/budget/{projectId}")
    @Operation(summary = "Generate budget insight for a project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiInsightDTO> generateBudgetInsight(
            @PathVariable Long projectId,
            Authentication authentication) {
        
        logger.info("Generating budget insight for project {} by user: {}", projectId, authentication.getName());
        AiInsightDTO insight = aiInsightService.generateBudgetInsight(projectId);
        return ResponseEntity.ok(insight);
    }
    
    @PostMapping("/generate/deadline/{projectId}")
    @Operation(summary = "Generate deadline insight for a project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiInsightDTO> generateDeadlineInsight(
            @PathVariable Long projectId,
            Authentication authentication) {
        
        logger.info("Generating deadline insight for project {} by user: {}", projectId, authentication.getName());
        AiInsightDTO insight = aiInsightService.generateDeadlineInsight(projectId);
        return ResponseEntity.ok(insight);
    }
}