package com.aipm.ai_project_management.modules.files.dto;

import com.aipm.ai_project_management.modules.files.entity.FileAttachment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class FileAttachmentDTO {
    
    private Long id;
    
    @NotBlank(message = "Original filename is required")
    private String originalFilename;
    
    @NotBlank(message = "Stored filename is required")
    private String storedFilename;
    
    @NotBlank(message = "File path is required")
    private String filePath;
    
    @NotNull(message = "File size is required")
    private Long fileSize;
    
    private String contentType;
    private String fileExtension;
    private FileAttachment.StorageType storageType;
    
    @NotNull(message = "Uploaded by user ID is required")
    private Long uploadedBy;
    
    private String entityType;
    private Long entityId;
    private String description;
    private Boolean isPublic;
    private Long downloadCount;
    private String checksum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for display
    private String uploaderName;
    private String humanReadableSize;
    private String downloadUrl;
    
    // Constructors
    public FileAttachmentDTO() {
    }
    
    public FileAttachmentDTO(String originalFilename, String storedFilename, String filePath, 
                           Long fileSize, String contentType, Long uploadedBy) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadedBy = uploadedBy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalFilename() {
        return originalFilename;
    }
    
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
    
    public String getStoredFilename() {
        return storedFilename;
    }
    
    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getFileExtension() {
        return fileExtension;
    }
    
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
    
    public FileAttachment.StorageType getStorageType() {
        return storageType;
    }
    
    public void setStorageType(FileAttachment.StorageType storageType) {
        this.storageType = storageType;
    }
    
    public Long getUploadedBy() {
        return uploadedBy;
    }
    
    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public Long getEntityId() {
        return entityId;
    }
    
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public Long getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public String getChecksum() {
        return checksum;
    }
    
    public void setChecksum(String checksum) {
        this.checksum = checksum;
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
    
    public String getUploaderName() {
        return uploaderName;
    }
    
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }
    
    public String getHumanReadableSize() {
        return humanReadableSize;
    }
    
    public void setHumanReadableSize(String humanReadableSize) {
        this.humanReadableSize = humanReadableSize;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}