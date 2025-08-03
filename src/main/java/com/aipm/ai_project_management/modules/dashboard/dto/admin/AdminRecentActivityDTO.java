package com.aipm.ai_project_management.modules.dashboard.dto.admin;

public class AdminRecentActivityDTO {
    private Long id;
    private String type;
    private String user;
    private String message;
    private String timestamp;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
