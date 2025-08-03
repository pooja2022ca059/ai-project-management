package com.aipm.ai_project_management.modules.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class RefreshTokenResponse {
    
    private String token;
    
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;
    
    // Default constructor (replaces @NoArgsConstructor)
    public RefreshTokenResponse() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public RefreshTokenResponse(String token, LocalDateTime expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }
    
    // Getters and setters (part of @Data replacement)
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    // equals method (part of @Data replacement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        RefreshTokenResponse that = (RefreshTokenResponse) o;
        
        if (!Objects.equals(token, that.token)) return false;
        return Objects.equals(expiresAt, that.expiresAt);
    }
    
    // hashCode method (part of @Data replacement)
    @Override
    public int hashCode() {
        return Objects.hash(token, expiresAt);
    }
    
    // toString method (part of @Data replacement)
    @Override
    public String toString() {
        return "RefreshTokenResponse{" +
                "token='" + (token != null ? "[PRESENT]" : "[NOT PRESENT]") + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String token;
        private LocalDateTime expiresAt;
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        
        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        
        public RefreshTokenResponse build() {
            return new RefreshTokenResponse(token, expiresAt);
        }
    }
}