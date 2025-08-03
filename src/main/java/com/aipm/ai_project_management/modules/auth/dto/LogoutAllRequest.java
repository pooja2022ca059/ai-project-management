package com.aipm.ai_project_management.modules.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class LogoutAllRequest {
    
    @NotNull(message = "User ID is required")
    @JsonProperty("user_id")
    private Long userId;
    
    // Default constructor (replaces @NoArgsConstructor)
    public LogoutAllRequest() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public LogoutAllRequest(Long userId) {
        this.userId = userId;
    }
    
    // Getter and setter (part of @Data replacement)
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    // equals method (part of @Data replacement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        LogoutAllRequest that = (LogoutAllRequest) o;
        
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }
    
    // hashCode method (part of @Data replacement)
    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
    
    // toString method (part of @Data replacement)
    @Override
    public String toString() {
        return "LogoutAllRequest{" +
                "userId=" + userId +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long userId;
        
        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        
        public LogoutAllRequest build() {
            return new LogoutAllRequest(userId);
        }
    }
}