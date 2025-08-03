package com.aipm.ai_project_management.modules.tasks.dto;

import java.time.LocalDateTime;

public class AttachmentDTO {
    
    private Long id;
    private Long taskId;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;
    private Long uploadedBy;
    private String uploaderName;
    private LocalDateTime uploadedAt;

    // Default constructor
    public AttachmentDTO() {
    }

    // Constructor with basic fields
    public AttachmentDTO(Long id, Long taskId, String fileName, String fileType, Long fileSize) {
        this.id = id;
        this.taskId = taskId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
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

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "AttachmentDTO{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", fileName='" + fileName + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileSize=" + fileSize +
                ", uploadedBy=" + uploadedBy +
                ", uploaderName='" + uploaderName + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
