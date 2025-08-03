package com.aipm.ai_project_management.modules.auth.dto;

import com.aipm.ai_project_management.common.enums.UserRole;

public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private UserRole role;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}