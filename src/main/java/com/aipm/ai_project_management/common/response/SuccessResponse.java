package com.aipm.ai_project_management.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {
    
    private boolean success = true;
    private String message;
    private LocalDateTime timestamp;
    
    // Constructors
    public SuccessResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public SuccessResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    // Static factory method
    public static SuccessResponse of(String message) {
        return new SuccessResponse(message);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
