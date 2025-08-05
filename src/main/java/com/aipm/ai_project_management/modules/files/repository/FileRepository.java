package com.aipm.ai_project_management.modules.files.repository;

import com.aipm.ai_project_management.modules.files.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    
    // Find files by project
    List<FileEntity> findByProjectIdAndIsActiveTrueOrderByCreatedAtDesc(Long projectId);
    Page<FileEntity> findByProjectIdAndIsActiveTrueOrderByCreatedAtDesc(Long projectId, Pageable pageable);
    
    // Find files by task
    List<FileEntity> findByTaskIdAndIsActiveTrueOrderByCreatedAtDesc(Long taskId);
    
    // Find files by uploader
    List<FileEntity> findByUploadedByAndIsActiveTrueOrderByCreatedAtDesc(Long uploadedBy);
    Page<FileEntity> findByUploadedByAndIsActiveTrueOrderByCreatedAtDesc(Long uploadedBy, Pageable pageable);
    
    // Find files by type
    List<FileEntity> findByFileTypeAndIsActiveTrueOrderByCreatedAtDesc(FileEntity.FileType fileType);
    
    // Find file by stored name
    Optional<FileEntity> findByStoredNameAndIsActiveTrue(String storedName);
    
    // Search files by name
    @Query("SELECT f FROM FileEntity f WHERE f.isActive = true AND " +
           "(LOWER(f.originalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY f.createdAt DESC")
    List<FileEntity> searchFilesByName(@Param("searchTerm") String searchTerm);
    
    // Find recent files
    @Query("SELECT f FROM FileEntity f WHERE f.isActive = true AND f.createdAt >= :since ORDER BY f.createdAt DESC")
    List<FileEntity> findRecentFiles(@Param("since") LocalDateTime since);
    
    // Find large files
    @Query("SELECT f FROM FileEntity f WHERE f.isActive = true AND f.fileSize > :sizeThreshold ORDER BY f.fileSize DESC")
    List<FileEntity> findLargeFiles(@Param("sizeThreshold") Long sizeThreshold);
    
    // Find popular files (most downloaded)
    @Query("SELECT f FROM FileEntity f WHERE f.isActive = true AND f.downloadCount > 0 ORDER BY f.downloadCount DESC")
    List<FileEntity> findPopularFiles(Pageable pageable);
    
    // Count files by project
    long countByProjectIdAndIsActiveTrue(Long projectId);
    
    // Count files by user
    long countByUploadedByAndIsActiveTrue(Long uploadedBy);
    
    // Calculate total file size by project
    @Query("SELECT SUM(f.fileSize) FROM FileEntity f WHERE f.projectId = :projectId AND f.isActive = true")
    Long getTotalFileSizeByProject(@Param("projectId") Long projectId);
    
    // Calculate total file size by user
    @Query("SELECT SUM(f.fileSize) FROM FileEntity f WHERE f.uploadedBy = :userId AND f.isActive = true")
    Long getTotalFileSizeByUser(@Param("userId") Long userId);
    
    // Find files by access level
    List<FileEntity> findByAccessLevelAndIsActiveTrueOrderByCreatedAtDesc(FileEntity.AccessLevel accessLevel);
    
    // Find files by project and access level
    List<FileEntity> findByProjectIdAndAccessLevelAndIsActiveTrueOrderByCreatedAtDesc(Long projectId, FileEntity.AccessLevel accessLevel);
    
    // Find files uploaded in date range
    @Query("SELECT f FROM FileEntity f WHERE f.isActive = true AND f.createdAt BETWEEN :startDate AND :endDate ORDER BY f.createdAt DESC")
    List<FileEntity> findFilesInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}