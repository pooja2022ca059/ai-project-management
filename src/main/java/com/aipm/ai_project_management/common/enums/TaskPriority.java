package com.aipm.ai_project_management.common.enums;

/**
 * Represents the priority level of a task.
 */
public enum TaskPriority {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    URGENT("Urgent", 4);
    
    private final String displayName;
    private final int level;
    
    TaskPriority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public boolean isHigherThan(TaskPriority other) {
        return this.level > other.level;
    }
    
    public boolean isLowerThan(TaskPriority other) {
        return this.level < other.level;
    }
}
