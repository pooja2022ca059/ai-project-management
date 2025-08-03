package com.aipm.ai_project_management.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private boolean rememberMe = false;
    
    private String twoFactorCode;
    
    // Default constructor (replaces @NoArgsConstructor)
    public LoginRequest() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public LoginRequest(String email, String password, boolean rememberMe, String twoFactorCode) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
        this.twoFactorCode = twoFactorCode;
    }
    
    // Getters and setters (part of @Data replacement)
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isRememberMe() {
        return rememberMe;
    }
    
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
    public String getTwoFactorCode() {
        return twoFactorCode;
    }
    
    public void setTwoFactorCode(String twoFactorCode) {
        this.twoFactorCode = twoFactorCode;
    }
    
    // equals method (part of @Data replacement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        LoginRequest that = (LoginRequest) o;
        
        if (rememberMe != that.rememberMe) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return twoFactorCode != null ? twoFactorCode.equals(that.twoFactorCode) : that.twoFactorCode == null;
    }
    
    // hashCode method (part of @Data replacement)
    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (rememberMe ? 1 : 0);
        result = 31 * result + (twoFactorCode != null ? twoFactorCode.hashCode() : 0);
        return result;
    }
    
    // toString method (part of @Data replacement)
    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" + // Don't include actual password in toString for security
                ", rememberMe=" + rememberMe +
                ", twoFactorCode='" + (twoFactorCode != null ? "[PRESENT]" : "[NOT PRESENT]") + '\'' +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String email;
        private String password;
        private boolean rememberMe = false;
        private String twoFactorCode;
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder rememberMe(boolean rememberMe) {
            this.rememberMe = rememberMe;
            return this;
        }
        
        public Builder twoFactorCode(String twoFactorCode) {
            this.twoFactorCode = twoFactorCode;
            return this;
        }
        
        public LoginRequest build() {
            return new LoginRequest(email, password, rememberMe, twoFactorCode);
        }
    }
}