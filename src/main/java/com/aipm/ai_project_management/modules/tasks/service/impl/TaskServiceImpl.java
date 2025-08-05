package com.aipm.ai_project_management.modules.tasks.service.impl;

import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.modules.tasks.dto.*;
import com.aipm.ai_project_management.modules.tasks.entity.Task;
import com.aipm.ai_project_management.modules.tasks.entity.Subtask;
import com.aipm.ai_project_management.modules.tasks.entity.TaskComment;
import com.aipm.ai_project_management.modules.tasks.repository.TaskRepository;
import com.aipm.ai_project_management.modules.tasks.repository.SubtaskRepository;
import com.aipm.ai_project_management.modules.tasks.repository.TaskCommentRepository;
import com.aipm.ai_project_management.modules.projects.repository.ProjectRepository;
import com.aipm.ai_project_management.modules.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = Logger.getLogger(TaskServiceImpl.class.getName());

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private SubtaskRepository subtaskRepository;
    
    @Autowired
    private TaskCommentRepository taskCommentRepository;
    
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public TaskDTO createTask(CreateTaskRequest request) {
        logger.info("Creating task for project: " + request.getProjectId());
        
        // 1. Validate project exists
        if (!projectRepository.existsById(request.getProjectId())) {
            throw new IllegalArgumentException("Project with ID " + request.getProjectId() + " not found");
        }
        
        // 2. Create task entity
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setPriority(request.getPriority());
        task.setProjectId(request.getProjectId());
        task.setReporterId(request.getReporterId());
        task.setAssigneeId(request.getAssigneeId());
        task.setDueDate(request.getDueDate());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setProgress(0);
        if (request.getLabels() != null) {
            task.setLabels(request.getLabels());
        }
        
        // 3. Save to database
        Task savedTask = taskRepository.save(task);
        
        // 4. Convert to DTO and return
        return convertToTaskDTO(savedTask);
    }

    @Override
    public SubtaskDTO createSubtask(Long taskId, CreateSubtaskRequest request) {
        logger.info("Creating subtask for task: " + taskId);
        
        // Validate task exists
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskId + " not found"));
        
        // Create subtask entity
        Subtask subtask = new Subtask();
        subtask.setTitle(request.getTitle());
        subtask.setTask(task);
        subtask.setStatus(TaskStatus.TODO);
        subtask.setAssigneeId(request.getAssigneeId());
        
        // Save to database
        Subtask savedSubtask = subtaskRepository.save(subtask);
        
        return convertToSubtaskDTO(savedSubtask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        logger.info("Fetching task by id: " + id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + id + " not found"));
        
        return convertToTaskDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDetailDTO getTaskDetails(Long id) {
        logger.info("Fetching task details for id: " + id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + id + " not found"));
        
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        taskDetailDTO.setId(task.getId());
        taskDetailDTO.setTitle(task.getTitle());
        taskDetailDTO.setDescription(task.getDescription());
        taskDetailDTO.setStatus(task.getStatus());
        taskDetailDTO.setPriority(task.getPriority());
        taskDetailDTO.setDueDate(task.getDueDate());
        taskDetailDTO.setEstimatedHours(task.getEstimatedHours());
        taskDetailDTO.setLoggedHours(task.getLoggedHours());
        taskDetailDTO.setProgress(task.getProgress());
        taskDetailDTO.setLabels(task.getLabels());
        taskDetailDTO.setCreatedAt(task.getCreatedAt());
        taskDetailDTO.setUpdatedAt(task.getUpdatedAt());
        
        // Load subtasks
        List<Subtask> subtasks = subtaskRepository.findByTaskId(id);
        taskDetailDTO.setSubtasks(subtasks.stream()
                .map(this::convertToSubtaskDTO)
                .collect(Collectors.toList()));
        
        // Load comments (convert to CommentDTO format expected by TaskDetailDTO)
        List<TaskComment> comments = taskCommentRepository.findByTaskIdAndParentIdIsNullOrderByCreatedAtAsc(id);
        taskDetailDTO.setComments(comments.stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList()));
        
        // Set empty attachments for now (will be implemented in file upload controller)
        taskDetailDTO.setAttachments(new ArrayList<>());
        
        return taskDetailDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> getTasksByProjectId(Long projectId, Map<String, String> filters, Pageable pageable) {
        logger.info("Fetching tasks for project: " + projectId + " with filters: " + filters);
        
        Page<Task> tasks;
        
        // Apply filters if provided
        if (filters != null && filters.containsKey("status")) {
            TaskStatus status = TaskStatus.valueOf(filters.get("status").toUpperCase());
            tasks = taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        } else {
            tasks = taskRepository.findByProjectId(projectId, pageable);
        }
        
        return tasks.map(this::convertToTaskDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskBoardDTO getTaskBoard(Long projectId, String view) {
        logger.info("Fetching task board for project: " + projectId + " with view: " + view);
        
        // Get all tasks for the project
        List<Task> tasks = taskRepository.findByProjectId(projectId, Pageable.unpaged()).getContent();
        
        TaskBoardDTO taskBoard = new TaskBoardDTO();
        taskBoard.setProjectId(projectId);
        taskBoard.setView(view);
        
        // Create columns for each status
        List<TaskBoardDTO.TaskColumnDTO> columns = new ArrayList<>();
        
        for (TaskStatus status : TaskStatus.values()) {
            TaskBoardDTO.TaskColumnDTO column = new TaskBoardDTO.TaskColumnDTO();
            column.setId(status.toString());
            column.setName(status.getDisplayName());
            
            List<TaskDTO> columnTasks = tasks.stream()
                    .filter(task -> task.getStatus() == status)
                    .map(this::convertToTaskDTO)
                    .collect(Collectors.toList());
            
            column.setTasks(columnTasks);
            column.setTaskCount(columnTasks.size());
            columns.add(column);
        }
        
        taskBoard.setColumns(columns);
        return taskBoard;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> getTasksAssignedToUser(Long userId, Map<String, String> filters, Pageable pageable) {
        logger.info("Fetching tasks assigned to user: " + userId + " with filters: " + filters);
        
        Page<Task> tasks;
        
        // Apply filters if provided
        if (filters != null && filters.containsKey("status")) {
            TaskStatus status = TaskStatus.valueOf(filters.get("status").toUpperCase());
            tasks = taskRepository.findByAssigneeIdAndStatus(userId, status, pageable);
        } else {
            tasks = taskRepository.findByAssigneeId(userId, pageable);
        }
        
        return tasks.map(this::convertToTaskDTO);
    }

    @Override
    public TaskDTO updateTask(Long id, UpdateTaskRequest request) {
        logger.info("Updating task: " + id);
        
        // 1. Find existing task
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + id + " not found"));
        
        // 2. Update fields
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getAssigneeId() != null) {
            task.setAssigneeId(request.getAssigneeId());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getEstimatedHours() != null) {
            task.setEstimatedHours(request.getEstimatedHours());
        }
        if (request.getProgress() != null) {
            task.setProgress(request.getProgress());
        }
        if (request.getLabels() != null) {
            task.setLabels(request.getLabels());
        }
        
        // 3. Save to database
        Task updatedTask = taskRepository.save(task);
        
        // 4. Return updated DTO
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public TaskDTO updateTaskStatus(Long id, TaskStatus status) {
        logger.info("Updating task status: " + id + " to " + status);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + id + " not found"));
        
        task.setStatus(status);
        
        // Auto-update progress based on status
        if (status == TaskStatus.DONE) {
            task.setProgress(100);
        } else if (status == TaskStatus.IN_PROGRESS && task.getProgress() == 0) {
            task.setProgress(10); // Give some initial progress
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public TaskDTO assignTask(Long taskId, Long userId) {
        logger.info("Assigning task: " + taskId + " to user: " + userId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskId + " not found"));
        
        task.setAssigneeId(userId);
        Task updatedTask = taskRepository.save(task);
        
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public TaskDTO updateTaskProgress(Long taskId, Integer progress) {
        logger.info("Updating task progress: " + taskId + " to " + progress + "%");
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskId + " not found"));
        
        task.setProgress(progress);
        
        // Auto-update status based on progress
        if (progress == 100) {
            task.setStatus(TaskStatus.DONE);
        } else if (progress > 0 && task.getStatus() == TaskStatus.TODO) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        logger.info("Deleting task: " + id);
        
        // 1. Check if task exists
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + id + " not found"));
        
        // 2. Delete from database (cascade deletes will handle subtasks, comments, etc.)
        taskRepository.delete(task);
    }

    @Override
    public List<TaskDTO> bulkUpdateTaskStatus(List<Long> taskIds, TaskStatus status) {
        logger.info("Bulk updating " + taskIds.size() + " tasks to status: " + status);
        
        List<TaskDTO> updatedTasks = new ArrayList<>();
        
        for (Long taskId : taskIds) {
            TaskDTO task = updateTaskStatus(taskId, status);
            updatedTasks.add(task);
        }
        
        return updatedTasks;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> searchTasks(Long projectId, String searchTerm, Pageable pageable) {
        logger.info("Searching tasks in project: " + projectId + " with term: " + searchTerm);
        
        Page<Task> tasks = taskRepository.searchByProjectId(projectId, searchTerm, pageable);
        return tasks.map(this::convertToTaskDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getOverdueTasks() {
        logger.info("Fetching overdue tasks");
        
        List<TaskStatus> completedStatuses = List.of(TaskStatus.DONE, TaskStatus.CANCELLED);
        List<Task> overdueTasks = taskRepository.findOverdueTasks(LocalDateTime.now(), completedStatuses);
        
        return overdueTasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getUpcomingDeadlines(Integer days) {
        logger.info("Fetching tasks with deadlines in next " + days + " days");
        
        // For now, implement basic logic - can be enhanced with custom query
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusDays(days);
        
        List<Task> allTasks = taskRepository.findAll();
        List<Task> upcomingTasks = allTasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> task.getDueDate().isAfter(now) && task.getDueDate().isBefore(future))
                .filter(task -> task.getStatus() != TaskStatus.DONE && task.getStatus() != TaskStatus.CANCELLED)
                .collect(Collectors.toList());
        
        return upcomingTasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }
    
    // Helper conversion methods
    private TaskDTO convertToTaskDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setProjectId(task.getProjectId());
        dto.setDueDate(task.getDueDate());
        dto.setEstimatedHours(task.getEstimatedHours());
        dto.setLoggedHours(task.getLoggedHours());
        dto.setProgress(task.getProgress());
        dto.setLabels(task.getLabels());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        
        // For now, skip setting UserSummaryDTO objects as they require additional queries
        // This can be enhanced later to load user details
        
        return dto;
    }
    
    private SubtaskDTO convertToSubtaskDTO(Subtask subtask) {
        SubtaskDTO dto = new SubtaskDTO();
        dto.setId(subtask.getId());
        dto.setTaskId(subtask.getTask().getId());
        dto.setTitle(subtask.getTitle());
        dto.setStatus(subtask.getStatus());
        dto.setCompleted(subtask.getStatus() == TaskStatus.DONE);
        dto.setCreatedAt(subtask.getCreatedAt());
        dto.setUpdatedAt(subtask.getUpdatedAt());
        return dto;
    }
    
    private TaskCommentDTO convertToTaskCommentDTO(TaskComment comment) {
        TaskCommentDTO dto = new TaskCommentDTO();
        dto.setId(comment.getId());
        dto.setTaskId(comment.getTask().getId());
        dto.setContent(comment.getContent());
        dto.setParentId(comment.getParentId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        
        // Load replies if this is a top-level comment
        if (comment.getParentId() == null) {
            List<TaskComment> replies = taskCommentRepository.findByParentIdOrderByCreatedAtAsc(comment.getId());
            dto.setReplies(replies.stream()
                    .map(this::convertToTaskCommentDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private CommentDTO convertToCommentDTO(TaskComment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setTaskId(comment.getTask().getId());
        dto.setUserId(comment.getAuthorId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setEdited(comment.getUpdatedAt() != null);
        
        // For now, skip setting userFullName as it requires additional user query
        
        return dto;
    }
}
