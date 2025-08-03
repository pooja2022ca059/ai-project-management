package com.aipm.ai_project_management.modules.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    
    @NotBlank(message = "Refresh token is required")
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    // Default constructor (replaces @NoArgsConstructor)
    public RefreshTokenRequest() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    // Getter and setter (part of @Data replacement)
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    // equals method (part of @Data replacement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        RefreshTokenRequest that = (RefreshTokenRequest) o;
        
        return refreshToken != null ? refreshToken.equals(that.refreshToken) : that.refreshToken == null;
    }
    
    // hashCode method (part of @Data replacement)
    @Override
    public int hashCode() {
        return refreshToken != null ? refreshToken.hashCode() : 0;
    }
    
    // toString method (part of @Data replacement)
    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
                "refreshToken='" + (refreshToken != null ? "[PRESENT]" : "[NOT PRESENT]") + '\'' +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String refreshToken;
        
        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        
        public RefreshTokenRequest build() {
            return new RefreshTokenRequest(refreshToken);
        }
    }
}