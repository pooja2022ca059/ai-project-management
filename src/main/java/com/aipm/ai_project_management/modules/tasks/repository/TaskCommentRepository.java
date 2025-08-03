package com.aipm.ai_project_management.modules.tasks.repository;

import com.aipm.ai_project_management.modules.tasks.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskIdAndParentIdIsNullOrderByCreatedAtAsc(Long taskId);
    List<TaskComment> findByParentIdOrderByCreatedAtAsc(Long parentId);
}
