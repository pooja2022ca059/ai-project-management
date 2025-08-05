package com.aipm.ai_project_management.modules.ai.service;

import com.aipm.ai_project_management.modules.ai.dto.AiChatRequestDTO;
import com.aipm.ai_project_management.modules.ai.dto.AiChatResponseDTO;

import java.util.List;

public interface AiChatService {
    
    // Main chat functionality
    AiChatResponseDTO chat(AiChatRequestDTO request, Long userId);
    
    // Specialized chat functions
    AiChatResponseDTO getProjectAdvice(Long projectId, String question, Long userId);
    AiChatResponseDTO getTaskSuggestions(Long taskId, String context, Long userId);
    AiChatResponseDTO analyzeProjectHealth(Long projectId, Long userId);
    AiChatResponseDTO optimizeWorkload(Long teamId, Long userId);
    
    // Context-aware responses
    AiChatResponseDTO chatWithContext(String message, String context, Long userId);
    
    // Conversation management
    String startNewConversation(Long userId);
    List<AiChatResponseDTO> getConversationHistory(String conversationId, Long userId);
    void clearConversation(String conversationId, Long userId);
    
    // AI capabilities info
    List<String> getAvailableCapabilities();
    String getAiModelInfo();
}