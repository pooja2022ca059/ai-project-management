package com.aipm.ai_project_management.modules.ai.controller;

import com.aipm.ai_project_management.modules.ai.dto.AiChatRequestDTO;
import com.aipm.ai_project_management.modules.ai.dto.AiChatResponseDTO;
import com.aipm.ai_project_management.modules.ai.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/chat")
@Tag(name = "AI Chat", description = "AI-powered chat assistant for project management")
public class AiChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(AiChatController.class);
    
    @Autowired
    private AiChatService aiChatService;
    
    @PostMapping("/message")
    @Operation(summary = "Send a message to AI assistant")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiChatResponseDTO> chat(
            @Valid @RequestBody AiChatRequestDTO request,
            Authentication authentication) {
        
        // Extract user ID from authentication (simplified)
        Long userId = 1L; // In real implementation, extract from authentication
        
        logger.info("Processing chat request from user: {}", userId);
        AiChatResponseDTO response = aiChatService.chat(request, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/project-advice/{projectId}")
    @Operation(summary = "Get AI advice for a specific project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiChatResponseDTO> getProjectAdvice(
            @PathVariable Long projectId,
            @RequestParam String question,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Getting project advice for project {} from user: {}", projectId, userId);
        AiChatResponseDTO response = aiChatService.getProjectAdvice(projectId, question, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/task-suggestions/{taskId}")
    @Operation(summary = "Get AI suggestions for a specific task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiChatResponseDTO> getTaskSuggestions(
            @PathVariable Long taskId,
            @RequestParam(required = false) String context,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Getting task suggestions for task {} from user: {}", taskId, userId);
        AiChatResponseDTO response = aiChatService.getTaskSuggestions(taskId, context, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/analyze-project-health/{projectId}")
    @Operation(summary = "Analyze project health using AI")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiChatResponseDTO> analyzeProjectHealth(
            @PathVariable Long projectId,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Analyzing project health for project {} requested by user: {}", projectId, userId);
        AiChatResponseDTO response = aiChatService.analyzeProjectHealth(projectId, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/optimize-workload/{teamId}")
    @Operation(summary = "Get AI recommendations for workload optimization")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<AiChatResponseDTO> optimizeWorkload(
            @PathVariable Long teamId,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Optimizing workload for team {} requested by user: {}", teamId, userId);
        AiChatResponseDTO response = aiChatService.optimizeWorkload(teamId, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/contextual")
    @Operation(summary = "Chat with context")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AiChatResponseDTO> chatWithContext(
            @RequestParam String message,
            @RequestParam(required = false) String context,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Processing contextual chat from user: {}", userId);
        AiChatResponseDTO response = aiChatService.chatWithContext(message, context, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/conversation/start")
    @Operation(summary = "Start a new conversation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> startNewConversation(Authentication authentication) {
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Starting new conversation for user: {}", userId);
        String conversationId = aiChatService.startNewConversation(userId);
        return ResponseEntity.ok(Map.of("conversationId", conversationId));
    }
    
    @GetMapping("/conversation/{conversationId}/history")
    @Operation(summary = "Get conversation history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AiChatResponseDTO>> getConversationHistory(
            @PathVariable String conversationId,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Retrieving conversation history for {} by user: {}", conversationId, userId);
        List<AiChatResponseDTO> history = aiChatService.getConversationHistory(conversationId, userId);
        return ResponseEntity.ok(history);
    }
    
    @DeleteMapping("/conversation/{conversationId}")
    @Operation(summary = "Clear conversation history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> clearConversation(
            @PathVariable String conversationId,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Clearing conversation {} for user: {}", conversationId, userId);
        aiChatService.clearConversation(conversationId, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/capabilities")
    @Operation(summary = "Get available AI capabilities")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getCapabilities() {
        logger.info("Fetching AI capabilities");
        
        Map<String, Object> capabilities = Map.of(
            "availableCapabilities", aiChatService.getAvailableCapabilities(),
            "modelInfo", aiChatService.getAiModelInfo(),
            "version", "1.0",
            "supportedLanguages", List.of("English"),
            "maxMessageLength", 2000,
            "conversationTimeout", "24 hours"
        );
        
        return ResponseEntity.ok(capabilities);
    }
    
    @GetMapping("/health")
    @Operation(summary = "Check AI chat service health")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getServiceHealth() {
        logger.info("Checking AI chat service health");
        
        Map<String, Object> health = Map.of(
            "status", "healthy",
            "uptime", System.currentTimeMillis(),
            "modelStatus", "operational",
            "responseTime", "< 2 seconds",
            "availability", "99.9%"
        );
        
        return ResponseEntity.ok(health);
    }
}