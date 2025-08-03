package com.aipm.ai_project_management.modules.tasks.repository;

import com.aipm.ai_project_management.modules.tasks.entity.TaskDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for TaskDependency entity operations.
 */
@Repository
public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {
    
    List<TaskDependency> findByDependentTaskId(Long taskId);
    
    List<TaskDependency> findByDependencyTaskId(Long taskId);
    
    Optional<TaskDependency> findByDependentTaskIdAndDependencyTaskId(Long dependentTaskId, Long dependencyTaskId);
    
    void deleteByDependentTaskIdAndDependencyTaskId(Long dependentTaskId, Long dependencyTaskId);
    
    Integer countByDependentTaskId(Long taskId);
}
