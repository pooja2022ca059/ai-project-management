package com.aipm.ai_project_management.modules.files.service;

import com.aipm.ai_project_management.modules.files.dto.FileAttachmentDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {
    
    /**
     * Store uploaded file
     */
    FileAttachmentDTO storeFile(MultipartFile file, Long uploadedBy, String entityType, Long entityId, String description);
    
    /**
     * Store file with custom filename
     */
    FileAttachmentDTO storeFile(MultipartFile file, String customFilename, Long uploadedBy, String entityType, Long entityId, String description);
    
    /**
     * Get file by ID
     */
    FileAttachmentDTO getFileById(Long fileId);
    
    /**
     * Download file as resource
     */
    Resource loadFileAsResource(String storedFilename) throws IOException;
    
    /**
     * Get files by entity
     */
    List<FileAttachmentDTO> getFilesByEntity(String entityType, Long entityId);
    
    /**
     * Get files uploaded by user
     */
    List<FileAttachmentDTO> getFilesByUploader(Long uploaderId);
    
    /**
     * Delete file
     */
    void deleteFile(Long fileId);
    
    /**
     * Delete files by entity
     */
    void deleteFilesByEntity(String entityType, Long entityId);
    
    /**
     * Get file statistics
     */
    FileStats getFileStatistics();
    
    /**
     * Validate file
     */
    void validateFile(MultipartFile file);
    
    /**
     * Get allowed file types
     */
    List<String> getAllowedFileTypes();
    
    /**
     * Check if file type is allowed
     */
    boolean isFileTypeAllowed(String contentType);
    
    /**
     * Get file size limit
     */
    long getMaxFileSize();
    
    /**
     * Inner class for file statistics
     */
    class FileStats {
        private long totalFiles;
        private long totalSize;
        private long todayUploads;
        
        public FileStats(long totalFiles, long totalSize, long todayUploads) {
            this.totalFiles = totalFiles;
            this.totalSize = totalSize;
            this.todayUploads = todayUploads;
        }
        
        // Getters
        public long getTotalFiles() { return totalFiles; }
        public long getTotalSize() { return totalSize; }
        public long getTodayUploads() { return todayUploads; }
    }
}