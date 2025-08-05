package com.aipm.ai_project_management.modules.files.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.files.dto.FileDTO;
import com.aipm.ai_project_management.modules.files.dto.FileUploadRequestDTO;
import com.aipm.ai_project_management.modules.files.entity.FileEntity;
import com.aipm.ai_project_management.modules.files.repository.FileRepository;
import com.aipm.ai_project_management.modules.files.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileServiceImpl implements FileService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    
    @Autowired
    private FileRepository fileRepository;
    
    @Value("${app.file.upload-dir:./uploads}")
    private String uploadDir;
    
    @Value("${app.file.max-size:10485760}") // 10MB default
    private long maxFileSize;
    
    private final List<String> allowedExtensions = Arrays.asList(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf",
        "jpg", "jpeg", "png", "gif", "bmp", "svg",
        "zip", "rar", "7z", "tar", "gz",
        "mp4", "avi", "mov", "wmv", "flv",
        "mp3", "wav", "flac", "aac"
    );
    
    @Override
    public FileDTO uploadFile(MultipartFile file, FileUploadRequestDTO request, Long uploadedBy) throws IOException {
        logger.info("Uploading file: {} for user: {}", file.getOriginalFilename(), uploadedBy);
        
        validateFile(file);
        
        // Generate unique stored name
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);
        String storedName = UUID.randomUUID().toString() + "." + fileExtension;
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Save file to disk
        Path filePath = uploadPath.resolve(storedName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Create file entity
        FileEntity fileEntity = new FileEntity();
        fileEntity.setOriginalName(originalFilename);
        fileEntity.setStoredName(storedName);
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setFileSize(file.getSize());
        fileEntity.setUploadedBy(uploadedBy);
        fileEntity.setProjectId(request.getProjectId());
        fileEntity.setTaskId(request.getTaskId());
        fileEntity.setFileType(request.getFileType() != null ? request.getFileType() : 
                              determineFileType(originalFilename, file.getContentType()));
        fileEntity.setAccessLevel(request.getAccessLevel());
        fileEntity.setDescription(request.getDescription());
        
        FileEntity savedEntity = fileRepository.save(fileEntity);
        
        logger.info("File uploaded successfully with ID: {}", savedEntity.getId());
        return mapEntityToDTO(savedEntity);
    }
    
    @Override
    public FileDTO uploadMultipleFiles(MultipartFile[] files, FileUploadRequestDTO request, Long uploadedBy) throws IOException {
        logger.info("Uploading {} files for user: {}", files.length, uploadedBy);
        
        // For simplicity, upload first file and return its DTO
        // In real implementation, you might want to return a list or batch result
        if (files.length > 0) {
            return uploadFile(files[0], request, uploadedBy);
        }
        
        throw new IllegalArgumentException("No files provided for upload");
    }
    
    @Override
    @Transactional(readOnly = true)
    public FileDTO getFileById(Long id) {
        logger.info("Fetching file with ID: {}", id);
        
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
        
        return mapEntityToDTO(fileEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FileDTO getFileByStoredName(String storedName) {
        logger.info("Fetching file with stored name: {}", storedName);
        
        FileEntity fileEntity = fileRepository.findByStoredNameAndIsActiveTrue(storedName)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with stored name: " + storedName));
        
        return mapEntityToDTO(fileEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Resource downloadFile(Long fileId, Long userId) throws IOException {
        logger.info("Downloading file ID: {} for user: {}", fileId, userId);
        
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        // Check access permissions
        if (!canUserAccessFile(fileId, userId)) {
            throw new IllegalArgumentException("User does not have access to this file");
        }
        
        // Increment download count
        fileEntity.incrementDownloadCount();
        fileRepository.save(fileEntity);
        
        return loadFileAsResource(fileEntity.getStoredName());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Resource downloadFileByStoredName(String storedName, Long userId) throws IOException {
        logger.info("Downloading file by stored name: {} for user: {}", storedName, userId);
        
        FileEntity fileEntity = fileRepository.findByStoredNameAndIsActiveTrue(storedName)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with stored name: " + storedName));
        
        return downloadFile(fileEntity.getId(), userId);
    }
    
    @Override
    public FileDTO updateFileInfo(Long id, FileDTO fileDTO, Long userId) {
        logger.info("Updating file info for ID: {} by user: {}", id, userId);
        
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
        
        // Check edit permissions
        if (!canUserEditFile(id, userId)) {
            throw new IllegalArgumentException("User does not have permission to edit this file");
        }
        
        // Update editable fields
        if (fileDTO.getDescription() != null) {
            fileEntity.setDescription(fileDTO.getDescription());
        }
        if (fileDTO.getAccessLevel() != null) {
            fileEntity.setAccessLevel(fileDTO.getAccessLevel());
        }
        if (fileDTO.getFileType() != null) {
            fileEntity.setFileType(fileDTO.getFileType());
        }
        
        FileEntity updatedEntity = fileRepository.save(fileEntity);
        logger.info("Updated file info for ID: {}", id);
        return mapEntityToDTO(updatedEntity);
    }
    
    @Override
    public void deleteFile(Long id, Long userId) {
        logger.info("Deleting file ID: {} by user: {}", id, userId);
        
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
        
        // Check delete permissions
        if (!canUserDeleteFile(id, userId)) {
            throw new IllegalArgumentException("User does not have permission to delete this file");
        }
        
        // Soft delete
        fileEntity.setIsActive(false);
        fileRepository.save(fileEntity);
        
        logger.info("File marked as deleted: {}", id);
    }
    
    @Override
    public void moveFileToProject(Long fileId, Long projectId, Long userId) {
        logger.info("Moving file {} to project {} by user: {}", fileId, projectId, userId);
        
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        if (!canUserEditFile(fileId, userId)) {
            throw new IllegalArgumentException("User does not have permission to move this file");
        }
        
        fileEntity.setProjectId(projectId);
        fileEntity.setTaskId(null); // Clear task association
        fileRepository.save(fileEntity);
        
        logger.info("File {} moved to project {}", fileId, projectId);
    }
    
    @Override
    public void moveFileToTask(Long fileId, Long taskId, Long userId) {
        logger.info("Moving file {} to task {} by user: {}", fileId, taskId, userId);
        
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
        
        if (!canUserEditFile(fileId, userId)) {
            throw new IllegalArgumentException("User does not have permission to move this file");
        }
        
        fileEntity.setTaskId(taskId);
        fileRepository.save(fileEntity);
        
        logger.info("File {} moved to task {}", fileId, taskId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getProjectFiles(Long projectId) {
        logger.info("Fetching files for project: {}", projectId);
        
        List<FileEntity> files = fileRepository.findByProjectIdAndIsActiveTrueOrderByCreatedAtDesc(projectId);
        return files.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<FileDTO> getProjectFiles(Long projectId, Pageable pageable) {
        logger.info("Fetching files for project: {} with pagination", projectId);
        
        Page<FileEntity> files = fileRepository.findByProjectIdAndIsActiveTrueOrderByCreatedAtDesc(projectId, pageable);
        return files.map(this::mapEntityToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getTaskFiles(Long taskId) {
        logger.info("Fetching files for task: {}", taskId);
        
        List<FileEntity> files = fileRepository.findByTaskIdAndIsActiveTrueOrderByCreatedAtDesc(taskId);
        return files.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getUserFiles(Long userId) {
        logger.info("Fetching files for user: {}", userId);
        
        List<FileEntity> files = fileRepository.findByUploadedByAndIsActiveTrueOrderByCreatedAtDesc(userId);
        return files.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<FileDTO> getUserFiles(Long userId, Pageable pageable) {
        logger.info("Fetching files for user: {} with pagination", userId);
        
        Page<FileEntity> files = fileRepository.findByUploadedByAndIsActiveTrueOrderByCreatedAtDesc(userId, pageable);
        return files.map(this::mapEntityToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> searchFiles(String searchTerm) {
        logger.info("Searching files with term: {}", searchTerm);
        
        List<FileEntity> files = fileRepository.searchFilesByName(searchTerm);
        return files.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getFilesByType(FileEntity.FileType fileType) {
        logger.info("Fetching files by type: {}", fileType);
        
        List<FileEntity> files = fileRepository.findByFileTypeAndIsActiveTrueOrderByCreatedAtDesc(fileType);
        return files.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getRecentFiles(int days) {
        logger.info("Fetching files from last {} days", days);
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<FileEntity> files = fileRepository.findRecentFiles(since);
        return files.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getPopularFiles(int limit) {
        logger.info("Fetching top {} popular files", limit);
        
        Pageable pageable = PageRequest.of(0, limit);
        List<FileEntity> files = fileRepository.findPopularFiles(pageable);
        return files.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    // Analytics methods
    @Override
    @Transactional(readOnly = true)
    public long getFileCount() {
        return fileRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getProjectFileCount(Long projectId) {
        return fileRepository.countByProjectIdAndIsActiveTrue(projectId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUserFileCount(Long userId) {
        return fileRepository.countByUploadedByAndIsActiveTrue(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalFileSize() {
        // This would require a custom query in real implementation
        return 0L; // Placeholder
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getProjectTotalFileSize(Long projectId) {
        Long totalSize = fileRepository.getTotalFileSizeByProject(projectId);
        return totalSize != null ? totalSize : 0L;
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUserTotalFileSize(Long userId) {
        Long totalSize = fileRepository.getTotalFileSizeByUser(userId);
        return totalSize != null ? totalSize : 0L;
    }
    
    // Access control methods
    @Override
    @Transactional(readOnly = true)
    public boolean canUserAccessFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElse(null);
        if (fileEntity == null || !fileEntity.getIsActive()) {
            return false;
        }
        
        // File uploader can always access
        if (fileEntity.getUploadedBy().equals(userId)) {
            return true;
        }
        
        // Check access level
        switch (fileEntity.getAccessLevel()) {
            case PUBLIC:
                return true;
            case PRIVATE:
                return false;
            case PROJECT_MEMBERS:
                // In real implementation, check if user is project member
                return true; // Placeholder
            case TASK_ASSIGNEES:
                // In real implementation, check if user is task assignee
                return true; // Placeholder
            default:
                return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canUserEditFile(Long fileId, Long userId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElse(null);
        if (fileEntity == null || !fileEntity.getIsActive()) {
            return false;
        }
        
        // Only uploader can edit for now
        // In real implementation, might include project managers
        return fileEntity.getUploadedBy().equals(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canUserDeleteFile(Long fileId, Long userId) {
        // Same logic as edit for now
        return canUserEditFile(fileId, userId);
    }
    
    // Utility methods
    @Override
    public String getFileContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        
        switch (extension) {
            case "pdf": return "application/pdf";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls": return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt": return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "txt": return "text/plain";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "gif": return "image/gif";
            case "svg": return "image/svg+xml";
            case "zip": return "application/zip";
            case "mp4": return "video/mp4";
            case "mp3": return "audio/mpeg";
            default: return "application/octet-stream";
        }
    }
    
    @Override
    public FileEntity.FileType determineFileType(String fileName, String contentType) {
        String extension = getFileExtension(fileName).toLowerCase();
        
        if (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "svg").contains(extension)) {
            return FileEntity.FileType.IMAGE;
        } else if (Arrays.asList("xls", "xlsx", "csv").contains(extension)) {
            return FileEntity.FileType.SPREADSHEET;
        } else if (Arrays.asList("ppt", "pptx").contains(extension)) {
            return FileEntity.FileType.PRESENTATION;
        } else if (Arrays.asList("zip", "rar", "7z", "tar", "gz").contains(extension)) {
            return FileEntity.FileType.ARCHIVE;
        } else if (Arrays.asList("mp4", "avi", "mov", "wmv", "flv").contains(extension)) {
            return FileEntity.FileType.VIDEO;
        } else if (Arrays.asList("mp3", "wav", "flac", "aac").contains(extension)) {
            return FileEntity.FileType.AUDIO;
        } else if (Arrays.asList("pdf", "doc", "docx", "txt", "rtf").contains(extension)) {
            return FileEntity.FileType.DOCUMENT;
        }
        
        return FileEntity.FileType.OTHER;
    }
    
    @Override
    public boolean isValidFileSize(long fileSize) {
        return fileSize > 0 && fileSize <= maxFileSize;
    }
    
    @Override
    public boolean isValidFileType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return allowedExtensions.contains(extension);
    }
    
    @Override
    public void cleanupOrphanedFiles() {
        logger.info("Cleaning up orphaned files");
        // Implementation would find files not referenced in database
        // and remove them from storage
    }
    
    @Override
    public void cleanupOldFiles(int daysOld) {
        logger.info("Cleaning up files older than {} days", daysOld);
        // Implementation would find files older than specified days
        // and remove them based on retention policy
    }
    
    // Private helper methods
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        if (!isValidFileSize(file.getSize())) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }
        
        if (!isValidFileType(file.getOriginalFilename())) {
            throw new IllegalArgumentException("File type not allowed");
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
    
    private Resource loadFileAsResource(String storedName) throws IOException {
        try {
            Path filePath = Paths.get(uploadDir).resolve(storedName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IOException("File not found or not readable: " + storedName);
            }
        } catch (MalformedURLException ex) {
            throw new IOException("File path is invalid: " + storedName, ex);
        }
    }
    
    private FileDTO mapEntityToDTO(FileEntity entity) {
        FileDTO dto = new FileDTO();
        dto.setId(entity.getId());
        dto.setOriginalName(entity.getOriginalName());
        dto.setStoredName(entity.getStoredName());
        dto.setFilePath(entity.getFilePath());
        dto.setContentType(entity.getContentType());
        dto.setFileSize(entity.getFileSize());
        dto.setFormattedFileSize(entity.getFormattedFileSize());
        dto.setUploadedBy(entity.getUploadedBy());
        dto.setProjectId(entity.getProjectId());
        dto.setTaskId(entity.getTaskId());
        dto.setFileType(entity.getFileType());
        dto.setAccessLevel(entity.getAccessLevel());
        dto.setDescription(entity.getDescription());
        dto.setDownloadCount(entity.getDownloadCount());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setFileExtension(entity.getFileExtension());
        dto.setDownloadUrl("/api/files/" + entity.getId() + "/download");
        
        // Set permissions (simplified - in real implementation, consider current user context)
        dto.setCanEdit(true); // Placeholder
        dto.setCanDelete(true); // Placeholder
        dto.setCanDownload(true); // Placeholder
        
        return dto;
    }
}