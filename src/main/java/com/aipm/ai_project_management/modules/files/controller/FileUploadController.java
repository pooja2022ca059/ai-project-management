package com.aipm.ai_project_management.modules.files.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling file uploads and document management.
 * Provides endpoints for uploading files, retrieving file information, and managing attachments.
 */
@RestController
@RequestMapping("/api/files")
@Tag(name = "File Management", description = "File upload and document management endpoints")
public class FileUploadController {

    @Operation(summary = "Upload file", description = "Upload a file and attach it to a project or task")
    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FileUploadResponseDTO>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "entityId", required = false) Long entityId,
            @RequestParam(value = "description", required = false) String description) {
        
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        
        // Validate file size (10MB limit)
        long maxFileSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size cannot exceed 10MB");
        }
        
        // Validate file type (basic validation)
        String contentType = file.getContentType();
        if (contentType == null || (!isAllowedFileType(contentType))) {
            throw new IllegalArgumentException("File type not allowed. Supported types: images, documents, archives");
        }
        
        // For now, return mock response - will be implemented with real file storage service
        FileUploadResponseDTO response = new FileUploadResponseDTO();
        response.setId(1L);
        response.setFileName(file.getOriginalFilename());
        response.setFileSize(file.getSize());
        response.setContentType(contentType);
        response.setUrl("/api/files/download/1"); // Mock URL
        response.setEntityType(entityType);
        response.setEntityId(entityId);
        response.setDescription(description);
        response.setUploadedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get files by entity", description = "Get all files attached to a specific entity (project/task)")
    @GetMapping("/entity/{entityType}/{entityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<FileInfoDTO>>> getFilesByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        // For now, return empty list - will be implemented with real file service
        List<FileInfoDTO> files = new ArrayList<>();
        
        return ResponseEntity.ok(ApiResponse.success(files));
    }

    @Operation(summary = "Download file", description = "Download a file by its ID")
    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        
        // For now, return empty response - will be implemented with real file service
        // This would typically return the file content with appropriate headers
        throw new RuntimeException("File download not yet implemented");
    }

    @Operation(summary = "Delete file", description = "Delete a file by its ID")
    @DeleteMapping("/{fileId}")
    @PreAuthorize("hasRole('USER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable Long fileId) {
        
        // For now, return success - will be implemented with real file service
        return ResponseEntity.ok(ApiResponse.success("File deleted successfully", null));
    }

    @Operation(summary = "Get file info", description = "Get information about a specific file")
    @GetMapping("/{fileId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FileInfoDTO>> getFileInfo(@PathVariable Long fileId) {
        
        // For now, return mock data - will be implemented with real file service
        FileInfoDTO fileInfo = new FileInfoDTO();
        fileInfo.setId(fileId);
        fileInfo.setFileName("sample-file.pdf");
        fileInfo.setFileSize(1024L);
        fileInfo.setContentType("application/pdf");
        fileInfo.setUploadedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(ApiResponse.success(fileInfo));
    }

    // Helper method to validate file types
    private boolean isAllowedFileType(String contentType) {
        return contentType.startsWith("image/") ||
               contentType.startsWith("application/pdf") ||
               contentType.startsWith("application/msword") ||
               contentType.startsWith("application/vnd.openxmlformats-officedocument") ||
               contentType.startsWith("text/") ||
               contentType.startsWith("application/zip") ||
               contentType.startsWith("application/x-zip");
    }

    // DTOs
    public static class FileUploadResponseDTO {
        private Long id;
        private String fileName;
        private Long fileSize;
        private String contentType;
        private String url;
        private String entityType;
        private Long entityId;
        private String description;
        private LocalDateTime uploadedAt;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public LocalDateTime getUploadedAt() {
            return uploadedAt;
        }

        public void setUploadedAt(LocalDateTime uploadedAt) {
            this.uploadedAt = uploadedAt;
        }
    }

    public static class FileInfoDTO {
        private Long id;
        private String fileName;
        private Long fileSize;
        private String contentType;
        private String description;
        private String entityType;
        private Long entityId;
        private Long uploadedByUserId;
        private String uploadedByUserName;
        private LocalDateTime uploadedAt;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public Long getUploadedByUserId() {
            return uploadedByUserId;
        }

        public void setUploadedByUserId(Long uploadedByUserId) {
            this.uploadedByUserId = uploadedByUserId;
        }

        public String getUploadedByUserName() {
            return uploadedByUserName;
        }

        public void setUploadedByUserName(String uploadedByUserName) {
            this.uploadedByUserName = uploadedByUserName;
        }

        public LocalDateTime getUploadedAt() {
            return uploadedAt;
        }

        public void setUploadedAt(LocalDateTime uploadedAt) {
            this.uploadedAt = uploadedAt;
        }
    }
}