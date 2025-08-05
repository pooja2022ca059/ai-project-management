package com.aipm.ai_project_management.modules.ai.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AiChatResponseDTO {
    
    private String response;
    
    private String conversationId;
    
    private LocalDateTime timestamp;
    
    private Double confidenceScore;
    
    private List<String> suggestedActions;
    
    private List<AiInsightDTO> relatedInsights;
    
    private String responseType; // TEXT, ACTION_SUGGESTION, INSIGHT_SUMMARY, etc.
    
    private String model; // Which AI model generated this response
    
    private Integer tokensUsed;
    
    private Long processingTimeMs;
    
    // Constructors
    public AiChatResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }
    
    public AiChatResponseDTO(String response) {
        this();
        this.response = response;
    }
    
    public AiChatResponseDTO(String response, String conversationId) {
        this(response);
        this.conversationId = conversationId;
    }
    
    // Getters and Setters
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Double getConfidenceScore() {
        return confidenceScore;
    }
    
    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
    
    public List<String> getSuggestedActions() {
        return suggestedActions;
    }
    
    public void setSuggestedActions(List<String> suggestedActions) {
        this.suggestedActions = suggestedActions;
    }
    
    public List<AiInsightDTO> getRelatedInsights() {
        return relatedInsights;
    }
    
    public void setRelatedInsights(List<AiInsightDTO> relatedInsights) {
        this.relatedInsights = relatedInsights;
    }
    
    public String getResponseType() {
        return responseType;
    }
    
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public Integer getTokensUsed() {
        return tokensUsed;
    }
    
    public void setTokensUsed(Integer tokensUsed) {
        this.tokensUsed = tokensUsed;
    }
    
    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
}