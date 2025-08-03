package com.aipm.ai_project_management.modules.users.dto;

import com.aipm.ai_project_management.modules.users.entity.UserPreference.DigestFrequency;
import jakarta.validation.constraints.Size;

import java.util.Map;

public class UpdateUserSettingsRequest {
    
    // Personal Information Updates
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;
    
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
    
    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    private String timezone;
    
    @Size(max = 10, message = "Language code must not exceed 10 characters")
    private String language;
    
    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    private String avatar;
    
    // Preference Updates
    private String theme; // light, dark
    private String dateFormat; // MM/DD/YYYY, DD/MM/YYYY, etc.
    private String timeFormat; // 12, 24
    private Integer itemsPerPage;
    private Map<String, Boolean> emailNotifications;
    private Map<String, Boolean> pushNotifications;
    private DigestFrequency digestFrequency;
    
    // Default constructor
    public UpdateUserSettingsRequest() {
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
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
