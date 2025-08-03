// UserRole.java
package com.aipm.ai_project_management.common.enums;

public enum UserRole {
    ADMIN("Administrator"),
    PROJECT_MANAGER("Project Manager"),
    TEAM_MEMBER("Team Member"),
    CLIENT("Client");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
