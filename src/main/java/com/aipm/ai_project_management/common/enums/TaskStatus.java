package com.aipm.ai_project_management.common.enums;

/**
 * Represents the status of a task in its lifecycle.
 */
public enum TaskStatus {
    BACKLOG("Backlog"),
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    TESTING("Testing"),
    DONE("Done"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    TaskStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public boolean isCompleted() {
        return this == DONE || this == CANCELLED;
    }
    
    public boolean isInProgress() {
        return this == IN_PROGRESS || this == IN_REVIEW || this == TESTING;
    }
}
