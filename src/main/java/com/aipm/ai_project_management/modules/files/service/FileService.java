package com.aipm.ai_project_management.modules.files.service;

import com.aipm.ai_project_management.modules.files.dto.FileDTO;
import com.aipm.ai_project_management.modules.files.dto.FileUploadRequestDTO;
import com.aipm.ai_project_management.modules.files.entity.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    
    // File upload and storage
    FileDTO uploadFile(MultipartFile file, FileUploadRequestDTO request, Long uploadedBy) throws IOException;
    FileDTO uploadMultipleFiles(MultipartFile[] files, FileUploadRequestDTO request, Long uploadedBy) throws IOException;
    
    // File retrieval
    FileDTO getFileById(Long id);
    FileDTO getFileByStoredName(String storedName);
    Resource downloadFile(Long fileId, Long userId) throws IOException;
    Resource downloadFileByStoredName(String storedName, Long userId) throws IOException;
    
    // File management
    FileDTO updateFileInfo(Long id, FileDTO fileDTO, Long userId);
    void deleteFile(Long id, Long userId);
    void moveFileToProject(Long fileId, Long projectId, Long userId);
    void moveFileToTask(Long fileId, Long taskId, Long userId);
    
    // File listings
    List<FileDTO> getProjectFiles(Long projectId);
    Page<FileDTO> getProjectFiles(Long projectId, Pageable pageable);
    List<FileDTO> getTaskFiles(Long taskId);
    List<FileDTO> getUserFiles(Long userId);
    Page<FileDTO> getUserFiles(Long userId, Pageable pageable);
    
    // File search and filtering
    List<FileDTO> searchFiles(String searchTerm);
    List<FileDTO> getFilesByType(FileEntity.FileType fileType);
    List<FileDTO> getRecentFiles(int days);
    List<FileDTO> getPopularFiles(int limit);
    
    // File analytics
    long getFileCount();
    long getProjectFileCount(Long projectId);
    long getUserFileCount(Long userId);
    long getTotalFileSize();
    long getProjectTotalFileSize(Long projectId);
    long getUserTotalFileSize(Long userId);
    
    // File access control
    boolean canUserAccessFile(Long fileId, Long userId);
    boolean canUserEditFile(Long fileId, Long userId);
    boolean canUserDeleteFile(Long fileId, Long userId);
    
    // File utilities
    String getFileContentType(String fileName);
    FileEntity.FileType determineFileType(String fileName, String contentType);
    boolean isValidFileSize(long fileSize);
    boolean isValidFileType(String fileName);
    
    // File cleanup
    void cleanupOrphanedFiles();
    void cleanupOldFiles(int daysOld);
}