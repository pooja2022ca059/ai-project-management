package com.aipm.ai_project_management.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    
    @NotBlank(message = "Token is required")
    private String token;
    
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String newPassword;
    
    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
    
    // Default constructor (replaces @NoArgsConstructor)
    public ResetPasswordRequest() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public ResetPasswordRequest(String token, String newPassword, String confirmPassword) {
        this.token = token;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    
    // Getters and setters (part of @Data replacement)
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    // equals method (part of @Data replacement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ResetPasswordRequest that = (ResetPasswordRequest) o;
        
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (newPassword != null ? !newPassword.equals(that.newPassword) : that.newPassword != null) return false;
        return confirmPassword != null ? confirmPassword.equals(that.confirmPassword) : that.confirmPassword == null;
    }
    
    // hashCode method (part of @Data replacement)
    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (newPassword != null ? newPassword.hashCode() : 0);
        result = 31 * result + (confirmPassword != null ? confirmPassword.hashCode() : 0);
        return result;
    }
    
    // toString method (part of @Data replacement) - with security in mind
    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "token='" + (token != null ? "[PRESENT]" : "[NOT PRESENT]") + '\'' +
                ", newPassword='[PROTECTED]'" +
                ", confirmPassword='[PROTECTED]'" +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String token;
        private String newPassword;
        private String confirmPassword;
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        
        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }
        
        public Builder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }
        
        public ResetPasswordRequest build() {
            return new ResetPasswordRequest(token, newPassword, confirmPassword);
        }
    }
}