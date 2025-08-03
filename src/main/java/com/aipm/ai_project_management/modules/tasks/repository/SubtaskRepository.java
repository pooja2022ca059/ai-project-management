package com.aipm.ai_project_management.modules.tasks.repository;

import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.modules.tasks.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Subtask entity operations.
 */
@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    
    List<Subtask> findByTaskId(Long taskId);
    
    List<Subtask> findByTaskIdAndStatus(Long taskId, TaskStatus status);
    
    List<Subtask> findByAssigneeId(Long assigneeId);
    
    @Query("SELECT COUNT(s) FROM Subtask s WHERE s.task.id = :taskId AND s.status = :status")
    Integer countByTaskIdAndStatus(@Param("taskId") Long taskId, @Param("status") TaskStatus status);
    
    @Query("SELECT COUNT(s) FROM Subtask s WHERE s.task.id = :taskId")
    Integer countByTaskId(@Param("taskId") Long taskId);
}
