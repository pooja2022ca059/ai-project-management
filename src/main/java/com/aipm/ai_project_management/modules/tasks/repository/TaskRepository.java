package com.aipm.ai_project_management.modules.tasks.repository;

import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.modules.tasks.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    
    List<Task> findByProjectId(Long projectId);
    
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);
    
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);
    
    Page<Task> findByAssigneeIdAndStatus(Long assigneeId, TaskStatus status, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status NOT IN :completedStatuses")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now, @Param("completedStatuses") List<TaskStatus> completedStatuses);
    
    @Query("SELECT t FROM Task t WHERE t.projectId = :projectId AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Task> searchByProjectId(@Param("projectId") Long projectId, 
                                @Param("searchTerm") String searchTerm,
                                Pageable pageable);
}
