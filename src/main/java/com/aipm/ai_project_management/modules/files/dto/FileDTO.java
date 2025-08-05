package com.aipm.ai_project_management.modules.files.dto;

import com.aipm.ai_project_management.modules.files.entity.FileEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class FileDTO {
    
    private Long id;
    
    @NotBlank(message = "Original name is required")
    private String originalName;
    
    private String storedName;
    
    private String filePath;
    
    private String contentType;
    
    private Long fileSize;
    
    private String formattedFileSize;
    
    @NotNull(message = "Uploader ID is required")
    private Long uploadedBy;
    
    private String uploaderName;
    
    private Long projectId;
    
    private String projectName;
    
    private Long taskId;
    
    private String taskName;
    
    private FileEntity.FileType fileType;
    
    private FileEntity.AccessLevel accessLevel;
    
    private String description;
    
    private Long downloadCount;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String fileExtension;
    
    private String downloadUrl;
    
    private Boolean canEdit;
    
    private Boolean canDelete;
    
    private Boolean canDownload;
    
    // Constructors
    public FileDTO() {
        this.fileType = FileEntity.FileType.DOCUMENT;
        this.accessLevel = FileEntity.AccessLevel.PROJECT_MEMBERS;
        this.downloadCount = 0L;
        this.isActive = true;
    }
    
    public FileDTO(String originalName, Long uploadedBy) {
        this();
        this.originalName = originalName;
        this.uploadedBy = uploadedBy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalName() {
        return originalName;
    }
    
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    
    public String getStoredName() {
        return storedName;
    }
    
    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getFormattedFileSize() {
        return formattedFileSize;
    }
    
    public void setFormattedFileSize(String formattedFileSize) {
        this.formattedFileSize = formattedFileSize;
    }
    
    public Long getUploadedBy() {
        return uploadedBy;
    }
    
    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
    
    public String getUploaderName() {
        return uploaderName;
    }
    
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
    
    public Long getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getFileExtension() {
        return fileExtension;
    }
    
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public Boolean getCanEdit() {
        return canEdit;
    }
    
    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }
    
    public Boolean getCanDelete() {
        return canDelete;
    }
    
    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
    
    public Boolean getCanDownload() {
        return canDownload;
    }
    
    public void setCanDownload(Boolean canDownload) {
        this.canDownload = canDownload;
    }
}