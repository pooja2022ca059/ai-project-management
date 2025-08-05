package com.aipm.ai_project_management.modules.ai.service.impl;

import com.aipm.ai_project_management.modules.ai.dto.AiChatRequestDTO;
import com.aipm.ai_project_management.modules.ai.dto.AiChatResponseDTO;
import com.aipm.ai_project_management.modules.ai.dto.AiInsightDTO;
import com.aipm.ai_project_management.modules.ai.service.AiChatService;
import com.aipm.ai_project_management.modules.ai.service.AiInsightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AiChatServiceImpl implements AiChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiChatServiceImpl.class);
    
    @Autowired
    private AiInsightService aiInsightService;
    
    // In-memory conversation storage (in production, use Redis or database)
    private final Map<String, List<AiChatResponseDTO>> conversationHistory = new ConcurrentHashMap<>();
    private final Map<String, Long> conversationOwners = new ConcurrentHashMap<>();
    
    @Override
    public AiChatResponseDTO chat(AiChatRequestDTO request, Long userId) {
        logger.info("Processing AI chat request from user: {}", userId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            String conversationId = request.getConversationId();
            if (conversationId == null) {
                conversationId = startNewConversation(userId);
            }
            
            // Validate conversation ownership
            if (!conversationOwners.get(conversationId).equals(userId)) {
                throw new IllegalArgumentException("Invalid conversation access");
            }
            
            // Generate AI response based on message type and context
            AiChatResponseDTO response = generateResponse(request, userId);
            response.setConversationId(conversationId);
            response.setProcessingTimeMs(System.currentTimeMillis() - startTime);
            response.setModel("AI_PROJECT_ASSISTANT_V1");
            
            // Store in conversation history
            conversationHistory.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(response);
            
            logger.info("Generated AI response for user {} in {}ms", userId, response.getProcessingTimeMs());
            return response;
            
        } catch (Exception e) {
            logger.error("Error processing AI chat request: {}", e.getMessage(), e);
            
            AiChatResponseDTO errorResponse = new AiChatResponseDTO();
            errorResponse.setResponse("I apologize, but I encountered an error processing your request. Please try again.");
            errorResponse.setResponseType("ERROR");
            errorResponse.setProcessingTimeMs(System.currentTimeMillis() - startTime);
            return errorResponse;
        }
    }
    
    @Override
    public AiChatResponseDTO getProjectAdvice(Long projectId, String question, Long userId) {
        logger.info("Generating project advice for project {} from user {}", projectId, userId);
        
        AiChatRequestDTO request = new AiChatRequestDTO(question);
        request.setProjectId(projectId);
        request.setChatType("PROJECT_ADVICE");
        request.setContext("project_analysis");
        
        // Get project insights to inform advice
        List<AiInsightDTO> projectInsights = aiInsightService.getInsightsForEntity("PROJECT", projectId);
        
        AiChatResponseDTO response = generateProjectSpecificResponse(question, projectId, projectInsights);
        response.setResponseType("PROJECT_ADVICE");
        response.setRelatedInsights(projectInsights);
        
        return response;
    }
    
    @Override
    public AiChatResponseDTO getTaskSuggestions(Long taskId, String context, Long userId) {
        logger.info("Generating task suggestions for task {} from user {}", taskId, userId);
        
        List<AiInsightDTO> taskInsights = aiInsightService.getInsightsForEntity("TASK", taskId);
        
        AiChatResponseDTO response = new AiChatResponseDTO();
        response.setResponse(generateTaskSuggestionResponse(taskId, context, taskInsights));
        response.setResponseType("TASK_SUGGESTIONS");
        response.setRelatedInsights(taskInsights);
        response.setSuggestedActions(Arrays.asList(
            "Break down into smaller subtasks",
            "Set milestone checkpoints",
            "Assign to team member with relevant skills",
            "Add time tracking"
        ));
        response.setConfidenceScore(0.82);
        
        return response;
    }
    
    @Override
    public AiChatResponseDTO analyzeProjectHealth(Long projectId, Long userId) {
        logger.info("Analyzing project health for project {} requested by user {}", projectId, userId);
        
        // Generate comprehensive health analysis
        List<AiInsightDTO> insights = aiInsightService.generateProjectInsights(projectId);
        
        AiChatResponseDTO response = new AiChatResponseDTO();
        response.setResponse(generateProjectHealthAnalysis(projectId, insights));
        response.setResponseType("PROJECT_HEALTH_ANALYSIS");
        response.setRelatedInsights(insights);
        response.setSuggestedActions(Arrays.asList(
            "Review resource allocation",
            "Check timeline feasibility",
            "Monitor budget consumption",
            "Assess team workload"
        ));
        response.setConfidenceScore(0.88);
        
        return response;
    }
    
    @Override
    public AiChatResponseDTO optimizeWorkload(Long teamId, Long userId) {
        logger.info("Optimizing workload for team {} requested by user {}", teamId, userId);
        
        List<AiInsightDTO> teamInsights = aiInsightService.generateTeamInsights(teamId);
        
        AiChatResponseDTO response = new AiChatResponseDTO();
        response.setResponse(generateWorkloadOptimizationAdvice(teamId, teamInsights));
        response.setResponseType("WORKLOAD_OPTIMIZATION");
        response.setRelatedInsights(teamInsights);
        response.setSuggestedActions(Arrays.asList(
            "Redistribute overloaded tasks",
            "Identify skill development opportunities",
            "Consider bringing additional resources",
            "Implement pair programming for knowledge transfer"
        ));
        response.setConfidenceScore(0.85);
        
        return response;
    }
    
    @Override
    public AiChatResponseDTO chatWithContext(String message, String context, Long userId) {
        logger.info("Processing contextual chat from user: {}", userId);
        
        AiChatRequestDTO request = new AiChatRequestDTO(message);
        request.setContext(context);
        request.setChatType("CONTEXTUAL");
        
        return chat(request, userId);
    }
    
    @Override
    public String startNewConversation(Long userId) {
        String conversationId = "conv_" + userId + "_" + System.currentTimeMillis();
        conversationOwners.put(conversationId, userId);
        conversationHistory.put(conversationId, new ArrayList<>());
        
        logger.info("Started new conversation {} for user {}", conversationId, userId);
        return conversationId;
    }
    
    @Override
    public List<AiChatResponseDTO> getConversationHistory(String conversationId, Long userId) {
        logger.info("Retrieving conversation history for {} by user {}", conversationId, userId);
        
        // Validate ownership
        if (!conversationOwners.get(conversationId).equals(userId)) {
            throw new IllegalArgumentException("Invalid conversation access");
        }
        
        return conversationHistory.getOrDefault(conversationId, new ArrayList<>());
    }
    
    @Override
    public void clearConversation(String conversationId, Long userId) {
        logger.info("Clearing conversation {} for user {}", conversationId, userId);
        
        // Validate ownership
        if (!conversationOwners.get(conversationId).equals(userId)) {
            throw new IllegalArgumentException("Invalid conversation access");
        }
        
        conversationHistory.remove(conversationId);
        conversationOwners.remove(conversationId);
    }
    
    @Override
    public List<String> getAvailableCapabilities() {
        return Arrays.asList(
            "Project Health Analysis",
            "Task Optimization Suggestions",
            "Workload Balancing",
            "Budget Forecasting",
            "Deadline Prediction",
            "Risk Assessment",
            "Resource Optimization",
            "Team Productivity Analysis",
            "Quality Prediction",
            "General Project Management Advice"
        );
    }
    
    @Override
    public String getAiModelInfo() {
        return "AI Project Assistant v1.0 - Specialized in project management insights and automation";
    }
    
    // Private helper methods for response generation
    private AiChatResponseDTO generateResponse(AiChatRequestDTO request, Long userId) {
        String message = request.getMessage().toLowerCase();
        String chatType = request.getChatType();
        
        AiChatResponseDTO response = new AiChatResponseDTO();
        
        // Analyze message intent and generate appropriate response
        if (chatType != null && chatType.equals("PROJECT_HELP")) {
            response.setResponse(generateProjectHelpResponse(request));
            response.setResponseType("PROJECT_HELP");
        } else if (message.contains("deadline") || message.contains("timeline")) {
            response.setResponse(generateDeadlineResponse(request));
            response.setResponseType("DEADLINE_ADVICE");
        } else if (message.contains("budget") || message.contains("cost")) {
            response.setResponse(generateBudgetResponse(request));
            response.setResponseType("BUDGET_ADVICE");
        } else if (message.contains("team") || message.contains("assign") || message.contains("workload")) {
            response.setResponse(generateTeamResponse(request));
            response.setResponseType("TEAM_ADVICE");
        } else if (message.contains("risk") || message.contains("problem")) {
            response.setResponse(generateRiskResponse(request));
            response.setResponseType("RISK_ANALYSIS");
        } else {
            response.setResponse(generateGeneralResponse(request));
            response.setResponseType("GENERAL");
        }
        
        response.setConfidenceScore(0.8 + Math.random() * 0.2); // Simulate confidence
        response.setTokensUsed((int) (message.length() * 1.2)); // Simulate token usage
        
        return response;
    }
    
    private AiChatResponseDTO generateProjectSpecificResponse(String question, Long projectId, List<AiInsightDTO> insights) {
        AiChatResponseDTO response = new AiChatResponseDTO();
        
        StringBuilder advice = new StringBuilder();
        advice.append("Based on analysis of project ").append(projectId).append(", here are my recommendations:\n\n");
        
        if (!insights.isEmpty()) {
            advice.append("Current insights suggest:\n");
            for (AiInsightDTO insight : insights) {
                advice.append("• ").append(insight.getTitle()).append(": ").append(insight.getDescription()).append("\n");
            }
            advice.append("\n");
        }
        
        advice.append("Regarding your question: \"").append(question).append("\"\n\n");
        advice.append("I recommend focusing on:\n");
        advice.append("• Regular progress monitoring and milestone tracking\n");
        advice.append("• Proactive risk identification and mitigation\n");
        advice.append("• Clear communication channels with stakeholders\n");
        advice.append("• Resource optimization based on current workload\n");
        
        response.setResponse(advice.toString());
        return response;
    }
    
    private String generateTaskSuggestionResponse(Long taskId, String context, List<AiInsightDTO> insights) {
        StringBuilder suggestions = new StringBuilder();
        suggestions.append("For task ").append(taskId).append(", I suggest:\n\n");
        
        if (context != null && !context.isEmpty()) {
            suggestions.append("Given the context: ").append(context).append("\n\n");
        }
        
        suggestions.append("Optimization recommendations:\n");
        suggestions.append("• Break complex tasks into smaller, manageable subtasks\n");
        suggestions.append("• Set clear acceptance criteria and definition of done\n");
        suggestions.append("• Assign based on team member skills and current workload\n");
        suggestions.append("• Implement regular check-ins to track progress\n");
        suggestions.append("• Consider dependencies and blockers early\n");
        
        if (!insights.isEmpty()) {
            suggestions.append("\nBased on AI analysis:\n");
            for (AiInsightDTO insight : insights) {
                suggestions.append("• ").append(insight.getDescription()).append("\n");
            }
        }
        
        return suggestions.toString();
    }
    
    private String generateProjectHealthAnalysis(Long projectId, List<AiInsightDTO> insights) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("Project Health Analysis for Project ").append(projectId).append(":\n\n");
        
        analysis.append("📊 Overall Status: ");
        double healthScore = Math.random(); // Simulate health calculation
        if (healthScore > 0.8) {
            analysis.append("EXCELLENT - Project is on track with minimal risks\n\n");
        } else if (healthScore > 0.6) {
            analysis.append("GOOD - Project is generally healthy with some areas for improvement\n\n");
        } else if (healthScore > 0.4) {
            analysis.append("MODERATE - Project needs attention in several areas\n\n");
        } else {
            analysis.append("CRITICAL - Immediate action required to get project back on track\n\n");
        }
        
        analysis.append("Key Metrics:\n");
        analysis.append("• Timeline Adherence: ").append(String.format("%.1f%%", healthScore * 100)).append("\n");
        analysis.append("• Budget Utilization: ").append(String.format("%.1f%%", (0.5 + Math.random() * 0.4) * 100)).append("\n");
        analysis.append("• Resource Efficiency: ").append(String.format("%.1f%%", (0.6 + Math.random() * 0.3) * 100)).append("\n");
        analysis.append("• Quality Score: ").append(String.format("%.1f%%", (0.7 + Math.random() * 0.3) * 100)).append("\n\n");
        
        if (!insights.isEmpty()) {
            analysis.append("AI-Generated Insights:\n");
            for (AiInsightDTO insight : insights) {
                analysis.append("• ").append(insight.getTitle()).append("\n");
            }
        }
        
        return analysis.toString();
    }
    
    private String generateWorkloadOptimizationAdvice(Long teamId, List<AiInsightDTO> insights) {
        StringBuilder advice = new StringBuilder();
        advice.append("Workload Optimization Analysis for Team ").append(teamId).append(":\n\n");
        
        advice.append("Current Analysis:\n");
        advice.append("• Team capacity utilization: ").append(String.format("%.1f%%", (0.7 + Math.random() * 0.3) * 100)).append("\n");
        advice.append("• Workload distribution: ").append(Math.random() > 0.5 ? "Balanced" : "Needs rebalancing").append("\n");
        advice.append("• Skill coverage: ").append(Math.random() > 0.6 ? "Adequate" : "Gaps identified").append("\n\n");
        
        advice.append("Recommendations:\n");
        advice.append("• Redistribute tasks from overloaded members to those with capacity\n");
        advice.append("• Cross-train team members to improve skill coverage\n");
        advice.append("• Implement daily standups for better workload visibility\n");
        advice.append("• Consider automation for repetitive tasks\n");
        advice.append("• Plan for buffer time in critical path activities\n\n");
        
        if (!insights.isEmpty()) {
            advice.append("Team Insights:\n");
            for (AiInsightDTO insight : insights) {
                advice.append("• ").append(insight.getDescription()).append("\n");
            }
        }
        
        return advice.toString();
    }
    
    // Simple response generators for different types
    private String generateProjectHelpResponse(AiChatRequestDTO request) {
        return "I'm here to help with your project management needs. I can provide insights on project health, suggest optimizations, help with resource planning, and answer questions about best practices. What specific aspect would you like assistance with?";
    }
    
    private String generateDeadlineResponse(AiChatRequestDTO request) {
        return "For deadline management, I recommend: 1) Breaking work into smaller milestones, 2) Building in buffer time for unexpected issues, 3) Regular progress reviews, 4) Early identification of blockers. Would you like me to analyze a specific project's timeline?";
    }
    
    private String generateBudgetResponse(AiChatRequestDTO request) {
        return "Budget management best practices include: 1) Regular tracking of actual vs. planned spend, 2) Setting up alerts at 75% and 90% thresholds, 3) Accounting for scope changes, 4) Planning for contingencies (10-20% buffer). I can help analyze your project's budget health.";
    }
    
    private String generateTeamResponse(AiChatRequestDTO request) {
        return "For team optimization: 1) Match tasks to team member skills and availability, 2) Ensure balanced workload distribution, 3) Facilitate knowledge sharing, 4) Monitor team velocity and adjust assignments. Would you like me to analyze your team's current workload?";
    }
    
    private String generateRiskResponse(AiChatRequestDTO request) {
        return "Risk management strategies: 1) Identify risks early through regular assessments, 2) Categorize by impact and probability, 3) Develop mitigation plans, 4) Monitor risk indicators continuously. I can help analyze potential risks in your project.";
    }
    
    private String generateGeneralResponse(AiChatRequestDTO request) {
        return "I'm your AI project management assistant. I can help with project planning, risk analysis, team optimization, budget tracking, and deadline management. Ask me about specific projects, tasks, or general project management best practices.";
    }
}