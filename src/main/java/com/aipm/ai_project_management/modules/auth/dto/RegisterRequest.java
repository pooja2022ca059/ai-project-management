package com.aipm.ai_project_management.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String password;
    
    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
    
    private String role = "TEAM_MEMBER";
    
    // Default constructor (replaces @NoArgsConstructor)
    public RegisterRequest() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public RegisterRequest(String name, String email, String password, String confirmPassword, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }
    
    // Getters and setters (part of @Data replacement)
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
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
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    // equals method (part of @Data replacement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        RegisterRequest that = (RegisterRequest) o;
        
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (confirmPassword != null ? !confirmPassword.equals(that.confirmPassword) : that.confirmPassword != null)
            return false;
        return role != null ? role.equals(that.role) : that.role == null;
    }
    
    // hashCode method (part of @Data replacement)
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (confirmPassword != null ? confirmPassword.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
    
    // toString method (part of @Data replacement) - with security in mind
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", confirmPassword='[PROTECTED]'" +
                ", role='" + role + '\'' +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String email;
        private String password;
        private String confirmPassword;
        private String role = "TEAM_MEMBER"; // Default value
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }
        
        public Builder role(String role) {
            this.role = role;
            return this;
        }
        
        public RegisterRequest build() {
            return new RegisterRequest(name, email, password, confirmPassword, role);
        }
    }
}