package com.aipm.ai_project_management.modules.users.entity;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
public class UserPreference extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;
    
    @Column(name = "theme")
    private String theme;
    
    @Column(name = "date_format")
    private String dateFormat;
    
    @Column(name = "time_format")
    private String timeFormat;
    
    @Column(name = "items_per_page")
    private Integer itemsPerPage;
    
    @Column(name = "email_notifications", columnDefinition = "JSON")
    private String emailNotifications;
    
    @Column(name = "push_notifications", columnDefinition = "JSON")
    private String pushNotifications;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "digest_frequency")
    private DigestFrequency digestFrequency;
    
    // Enum for digest frequency
    public enum DigestFrequency {
        NEVER, DAILY, WEEKLY
    }
    
    // Default constructor
    public UserPreference() {
        this.theme = "light";
        this.dateFormat = "MM/DD/YYYY";
        this.timeFormat = "12";
        this.itemsPerPage = 20;
        this.digestFrequency = DigestFrequency.DAILY;
        setDefaultNotifications();
    }
    
    // Constructor with user
    public UserPreference(User user) {
        this();
        this.user = user;
    }
    
    // Set default notification preferences
    private void setDefaultNotifications() {
        this.emailNotifications = "{\"task_assigned\":true,\"task_completed\":true,\"project_updates\":true,\"mentions\":true,\"deadlines\":true}";
        this.pushNotifications = "{\"task_assigned\":true,\"mentions\":true,\"urgent_updates\":true}";
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public String getEmailNotifications() {
        return emailNotifications;
    }
    
    public void setEmailNotifications(String emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
    
    public String getPushNotifications() {
        return pushNotifications;
    }
    
    public void setPushNotifications(String pushNotifications) {
        this.pushNotifications = pushNotifications;
    }
    
    public DigestFrequency getDigestFrequency() {
        return digestFrequency;
    }
    
    public void setDigestFrequency(DigestFrequency digestFrequency) {
        this.digestFrequency = digestFrequency;
    }
    
    // toString method
    @Override
    public String toString() {
        return "UserPreference{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", dateFormat='" + dateFormat + '\'' +
                ", timeFormat='" + timeFormat + '\'' +
                ", itemsPerPage=" + itemsPerPage +
                ", digestFrequency=" + digestFrequency +
                '}';
    }
}
