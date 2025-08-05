package com.aipm.ai_project_management.modules.files.controller;

import com.aipm.ai_project_management.modules.files.dto.FileDTO;
import com.aipm.ai_project_management.modules.files.dto.FileUploadRequestDTO;
import com.aipm.ai_project_management.modules.files.entity.FileEntity;
import com.aipm.ai_project_management.modules.files.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Management", description = "File upload, download, and management operations")
public class FileController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    @Autowired
    private FileService fileService;
    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Valid FileUploadRequestDTO request,
            Authentication authentication) {
        
        // Extract user ID from authentication (simplified)
        Long userId = 1L; // In real implementation, extract from authentication
        
        logger.info("Uploading file: {} by user: {}", file.getOriginalFilename(), userId);
        
        try {
            FileDTO fileDTO = fileService.uploadFile(file, request, userId);
            return new ResponseEntity<>(fileDTO, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("Failed to upload file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple files")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FileDTO> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @ModelAttribute @Valid FileUploadRequestDTO request,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Uploading {} files by user: {}", files.length, userId);
        
        try {
            FileDTO fileDTO = fileService.uploadMultipleFiles(files, request, userId);
            return new ResponseEntity<>(fileDTO, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("Failed to upload files: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get file information by ID")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FileDTO> getFileById(@PathVariable Long id) {
        logger.info("Fetching file with ID: {}", id);
        
        FileDTO fileDTO = fileService.getFileById(id);
        return ResponseEntity.ok(fileDTO);
    }
    
    @GetMapping("/{id}/download")
    @Operation(summary = "Download file by ID")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Downloading file ID: {} for user: {}", id, userId);
        
        try {
            FileDTO fileDTO = fileService.getFileById(id);
            Resource resource = fileService.downloadFile(id, userId);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileDTO.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename=\"" + fileDTO.getOriginalName() + "\"")
                    .body(resource);
                    
        } catch (IOException e) {
            logger.error("Failed to download file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/download/{storedName}")
    @Operation(summary = "Download file by stored name")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadFileByStoredName(
            @PathVariable String storedName,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Downloading file by stored name: {} for user: {}", storedName, userId);
        
        try {
            FileDTO fileDTO = fileService.getFileByStoredName(storedName);
            Resource resource = fileService.downloadFileByStoredName(storedName, userId);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileDTO.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename=\"" + fileDTO.getOriginalName() + "\"")
                    .body(resource);
                    
        } catch (IOException e) {
            logger.error("Failed to download file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update file information")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FileDTO> updateFileInfo(
            @PathVariable Long id,
            @RequestBody @Valid FileDTO fileDTO,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Updating file {} by user: {}", id, userId);
        
        FileDTO updatedFile = fileService.updateFileInfo(id, fileDTO, userId);
        return ResponseEntity.ok(updatedFile);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a file")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Deleting file {} by user: {}", id, userId);
        
        fileService.deleteFile(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/move-to-project/{projectId}")
    @Operation(summary = "Move file to a project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> moveFileToProject(
            @PathVariable Long id,
            @PathVariable Long projectId,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Moving file {} to project {} by user: {}", id, projectId, userId);
        
        fileService.moveFileToProject(id, projectId, userId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/move-to-task/{taskId}")
    @Operation(summary = "Move file to a task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> moveFileToTask(
            @PathVariable Long id,
            @PathVariable Long taskId,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Moving file {} to task {} by user: {}", id, taskId, userId);
        
        fileService.moveFileToTask(id, taskId, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get files for a project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<FileDTO>> getProjectFiles(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("Fetching files for project: {}", projectId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<FileDTO> files = fileService.getProjectFiles(projectId, pageable);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get files for a task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FileDTO>> getTaskFiles(@PathVariable Long taskId) {
        logger.info("Fetching files for task: {}", taskId);
        
        List<FileDTO> files = fileService.getTaskFiles(taskId);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get files uploaded by a user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<FileDTO>> getUserFiles(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("Fetching files for user: {}", userId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<FileDTO> files = fileService.getUserFiles(userId, pageable);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/my-files")
    @Operation(summary = "Get current user's files")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<FileDTO>> getMyFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Fetching files for current user: {}", userId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<FileDTO> files = fileService.getUserFiles(userId, pageable);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search files")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FileDTO>> searchFiles(
            @RequestParam String q) {
        
        logger.info("Searching files with term: {}", q);
        
        List<FileDTO> files = fileService.searchFiles(q);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/type/{fileType}")
    @Operation(summary = "Get files by type")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FileDTO>> getFilesByType(@PathVariable FileEntity.FileType fileType) {
        logger.info("Fetching files by type: {}", fileType);
        
        List<FileDTO> files = fileService.getFilesByType(fileType);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/recent")
    @Operation(summary = "Get recent files")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FileDTO>> getRecentFiles(
            @RequestParam(defaultValue = "7") int days) {
        
        logger.info("Fetching files from last {} days", days);
        
        List<FileDTO> files = fileService.getRecentFiles(days);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/popular")
    @Operation(summary = "Get popular files")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FileDTO>> getPopularFiles(
            @RequestParam(defaultValue = "10") int limit) {
        
        logger.info("Fetching top {} popular files", limit);
        
        List<FileDTO> files = fileService.getPopularFiles(limit);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/analytics")
    @Operation(summary = "Get file analytics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getFileAnalytics() {
        logger.info("Fetching file analytics");
        
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalFiles", fileService.getFileCount());
        analytics.put("totalFileSize", fileService.getTotalFileSize());
        analytics.put("documentFiles", fileService.getFilesByType(FileEntity.FileType.DOCUMENT).size());
        analytics.put("imageFiles", fileService.getFilesByType(FileEntity.FileType.IMAGE).size());
        analytics.put("spreadsheetFiles", fileService.getFilesByType(FileEntity.FileType.SPREADSHEET).size());
        analytics.put("presentationFiles", fileService.getFilesByType(FileEntity.FileType.PRESENTATION).size());
        analytics.put("archiveFiles", fileService.getFilesByType(FileEntity.FileType.ARCHIVE).size());
        analytics.put("videoFiles", fileService.getFilesByType(FileEntity.FileType.VIDEO).size());
        analytics.put("audioFiles", fileService.getFilesByType(FileEntity.FileType.AUDIO).size());
        analytics.put("otherFiles", fileService.getFilesByType(FileEntity.FileType.OTHER).size());
        
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/project/{projectId}/analytics")
    @Operation(summary = "Get project file analytics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getProjectFileAnalytics(@PathVariable Long projectId) {
        logger.info("Fetching file analytics for project: {}", projectId);
        
        Map<String, Object> analytics = Map.of(
            "totalFiles", fileService.getProjectFileCount(projectId),
            "totalFileSize", fileService.getProjectTotalFileSize(projectId)
        );
        
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/{id}/access-check")
    @Operation(summary = "Check if user can access file")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Boolean>> checkFileAccess(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Checking file access for file {} and user: {}", id, userId);
        
        Map<String, Boolean> access = Map.of(
            "canAccess", fileService.canUserAccessFile(id, userId),
            "canEdit", fileService.canUserEditFile(id, userId),
            "canDelete", fileService.canUserDeleteFile(id, userId)
        );
        
        return ResponseEntity.ok(access);
    }
    
    @PostMapping("/cleanup/orphaned")
    @Operation(summary = "Cleanup orphaned files")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cleanupOrphanedFiles(Authentication authentication) {
        logger.info("Cleaning up orphaned files by admin: {}", authentication.getName());
        
        fileService.cleanupOrphanedFiles();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/cleanup/old")
    @Operation(summary = "Cleanup old files")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cleanupOldFiles(
            @RequestParam(defaultValue = "365") int daysOld,
            Authentication authentication) {
        
        logger.info("Cleaning up files older than {} days by admin: {}", daysOld, authentication.getName());
        
        fileService.cleanupOldFiles(daysOld);
        return ResponseEntity.ok().build();
    }
}