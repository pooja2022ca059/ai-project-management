package com.aipm.ai_project_management.modules.projects.repository;

import com.aipm.ai_project_management.modules.projects.entity.ProjectDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectDocumentRepository extends JpaRepository<ProjectDocument, Long> {
    
    // Find documents by project
    Page<ProjectDocument> findByProjectId(Long projectId, Pageable pageable);
    
    // Find documents by project and file type
    List<ProjectDocument> findByProjectIdAndFileType(Long projectId, String fileType);
    
    // Find documents uploaded by user
    Page<ProjectDocument> findByUploadedBy(Long uploadedBy, Pageable pageable);
    
    // Find documents by name containing (case insensitive)
    Page<ProjectDocument> findByProjectIdAndNameContainingIgnoreCase(Long projectId, String name, Pageable pageable);
    
    // Find documents by version
    List<ProjectDocument> findByProjectIdAndVersion(Long projectId, String version);
    
    // Find latest documents by project
    @Query("SELECT d FROM ProjectDocument d WHERE d.project.id = :projectId ORDER BY d.createdAt DESC")
    Page<ProjectDocument> findLatestDocumentsByProject(@Param("projectId") Long projectId, Pageable pageable);
    
    // Find documents by date range
    @Query("SELECT d FROM ProjectDocument d WHERE d.project.id = :projectId AND d.createdAt BETWEEN :startDate AND :endDate")
    List<ProjectDocument> findByProjectAndDateRange(@Param("projectId") Long projectId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Count documents by project
    long countByProjectId(Long projectId);
    
    // Count documents by file type
    long countByProjectIdAndFileType(Long projectId, String fileType);
    
    // Find documents by file size range
    @Query("SELECT d FROM ProjectDocument d WHERE d.project.id = :projectId AND d.fileSize BETWEEN :minSize AND :maxSize")
    List<ProjectDocument> findByProjectAndFileSizeRange(@Param("projectId") Long projectId, @Param("minSize") Long minSize, @Param("maxSize") Long maxSize);
    
    // Check if document name exists in project
    boolean existsByProjectIdAndNameIgnoreCase(Long projectId, String name);
    
    // Check if document name exists in project excluding current document
    boolean existsByProjectIdAndNameIgnoreCaseAndIdNot(Long projectId, String name, Long id);
    
    // Get total file size by project
    @Query("SELECT COALESCE(SUM(d.fileSize), 0) FROM ProjectDocument d WHERE d.project.id = :projectId")
    Long getTotalFileSizeByProject(@Param("projectId") Long projectId);
    
    // Find documents by multiple file types
    @Query("SELECT d FROM ProjectDocument d WHERE d.project.id = :projectId AND d.fileType IN :fileTypes")
    List<ProjectDocument> findByProjectAndFileTypes(@Param("projectId") Long projectId, @Param("fileTypes") List<String> fileTypes);
}
