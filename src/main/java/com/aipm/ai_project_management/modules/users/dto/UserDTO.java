package com.aipm.ai_project_management.modules.users.dto;

import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.modules.auth.entity.User.UserStatus;

import java.time.LocalDateTime;

public class UserDTO {
    
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private UserRole role;
    private UserStatus status;
    private String phone;
    private String location;
    private String timezone;
    private String language;
    private Boolean emailVerified;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private boolean active;
    
    // Default constructor
    public UserDTO() {
    }
    
    // Constructor for mapping from User entity
    public UserDTO(Long id, String email, String name, String avatar, UserRole role, 
                   UserStatus status, String phone, String location, String timezone, 
                   String language, Boolean emailVerified, LocalDateTime lastLoginAt, 
                   LocalDateTime createdAt, boolean active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
        this.status = status;
        this.phone = phone;
        this.location = location;
        this.timezone = timezone;
        this.language = language;
        this.emailVerified = emailVerified;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.active = active;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
