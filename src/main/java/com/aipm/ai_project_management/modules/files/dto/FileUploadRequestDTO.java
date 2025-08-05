package com.aipm.ai_project_management.modules.files.dto;

import com.aipm.ai_project_management.modules.files.entity.FileEntity;
import jakarta.validation.constraints.NotNull;

public class FileUploadRequestDTO {
    
    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    private Long taskId;
    
    private FileEntity.FileType fileType;
    
    private FileEntity.AccessLevel accessLevel;
    
    private String description;
    
    // Constructors
    public FileUploadRequestDTO() {
        this.fileType = FileEntity.FileType.DOCUMENT;
        this.accessLevel = FileEntity.AccessLevel.PROJECT_MEMBERS;
    }
    
    public FileUploadRequestDTO(Long projectId) {
        this();
        this.projectId = projectId;
    }
    
    // Getters and Setters
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public FileEntity.FileType getFileType() {
        return fileType;
    }
    
    public void setFileType(FileEntity.FileType fileType) {
        this.fileType = fileType;
    }
    
    public FileEntity.AccessLevel getAccessLevel() {
        return accessLevel;
    }
    
    public void setAccessLevel(FileEntity.AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}