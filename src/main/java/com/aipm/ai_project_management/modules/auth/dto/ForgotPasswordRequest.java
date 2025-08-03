package com.aipm.ai_project_management.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    // Default constructor (replaces @NoArgsConstructor)
    public ForgotPasswordRequest() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public ForgotPasswordRequest(String email) {
        this.email = email;
    }
    
    // Getter and setter (part of @Data replacement)
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // equals method (part of @Data replacement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ForgotPasswordRequest that = (ForgotPasswordRequest) o;
        
        return email != null ? email.equals(that.email) : that.email == null;
    }
    
    // hashCode method (part of @Data replacement)
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
    
    // toString method (part of @Data replacement)
    @Override
    public String toString() {
        return "ForgotPasswordRequest{" +
                "email='" + email + '\'' +
                '}';
    }
    
    // Static builder class (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String email;
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public ForgotPasswordRequest build() {
            return new ForgotPasswordRequest(email);
        }
    }
}