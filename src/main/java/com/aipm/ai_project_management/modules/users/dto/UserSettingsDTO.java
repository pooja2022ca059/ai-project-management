package com.aipm.ai_project_management.modules.users.dto;

import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.modules.auth.entity.User.UserStatus;
import com.aipm.ai_project_management.modules.users.entity.UserPreference.DigestFrequency;

import java.time.LocalDateTime;
import java.util.Map;

public class UserSettingsDTO {
    
    // Personal Information
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
    
    // Preferences
    private String theme;
    private String dateFormat;
    private String timeFormat;
    private Integer itemsPerPage;
    private Map<String, Boolean> emailNotifications;
    private Map<String, Boolean> pushNotifications;
    private DigestFrequency digestFrequency;
    
    // Default constructor
    public UserSettingsDTO() {
    }
    
    // Getters and Setters - Personal Information
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
    
    // Getters and Setters - Preferences
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public String getDateFormat() {
        return dateFormat;
    }
    
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    
    public String getTimeFormat() {
        return timeFormat;
    }
    
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }
    
    public Integer getItemsPerPage() {
        return itemsPerPage;
    }
    
    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
    
    public Map<String, Boolean> getEmailNotifications() {
        return emailNotifications;
    }
    
    public void setEmailNotifications(Map<String, Boolean> emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
    
    public Map<String, Boolean> getPushNotifications() {
        return pushNotifications;
    }
    
    public void setPushNotifications(Map<String, Boolean> pushNotifications) {
        this.pushNotifications = pushNotifications;
    }
    
    public DigestFrequency getDigestFrequency() {
        return digestFrequency;
    }
    
    public void setDigestFrequency(DigestFrequency digestFrequency) {
        this.digestFrequency = digestFrequency;
    }
}
