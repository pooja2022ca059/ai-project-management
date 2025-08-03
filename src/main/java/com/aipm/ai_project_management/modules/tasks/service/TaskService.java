package com.aipm.ai_project_management.modules.tasks.service;

import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.modules.tasks.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TaskService {
    
    // Create operations
    TaskDTO createTask(CreateTaskRequest request);
    
    SubtaskDTO createSubtask(Long taskId, CreateSubtaskRequest request);
    
    // Read operations
    TaskDTO getTaskById(Long id);
    
    TaskDetailDTO getTaskDetails(Long id);
    
    Page<TaskDTO> getTasksByProjectId(Long projectId, Map<String, String> filters, Pageable pageable);
    
    TaskBoardDTO getTaskBoard(Long projectId, String view);
    
    Page<TaskDTO> getTasksAssignedToUser(Long userId, Map<String, String> filters, Pageable pageable);
    
    // Update operations
    TaskDTO updateTask(Long id, UpdateTaskRequest request);
    
    TaskDTO updateTaskStatus(Long id, TaskStatus status);
    
    TaskDTO assignTask(Long taskId, Long userId);
    
    TaskDTO updateTaskProgress(Long taskId, Integer progress);
    
    // Delete operations
    void deleteTask(Long id);
    
    // Bulk operations
    List<TaskDTO> bulkUpdateTaskStatus(List<Long> taskIds, TaskStatus status);
    
    // Search operations
    Page<TaskDTO> searchTasks(Long projectId, String searchTerm, Pageable pageable);
    
    // Additional operations
    List<TaskDTO> getOverdueTasks();
    
    List<TaskDTO> getUpcomingDeadlines(Integer days);
}
