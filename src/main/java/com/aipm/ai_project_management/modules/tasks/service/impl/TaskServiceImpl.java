package com.aipm.ai_project_management.modules.tasks.service.impl;

import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.tasks.dto.*;
import com.aipm.ai_project_management.modules.tasks.entity.Task;
import com.aipm.ai_project_management.modules.tasks.entity.Subtask;
import com.aipm.ai_project_management.modules.tasks.entity.TaskDependency;
import com.aipm.ai_project_management.modules.tasks.repository.TaskRepository;
import com.aipm.ai_project_management.modules.tasks.repository.SubtaskRepository;
import com.aipm.ai_project_management.modules.tasks.repository.TaskDependencyRepository;
import com.aipm.ai_project_management.modules.projects.repository.ProjectRepository;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = Logger.getLogger(TaskServiceImpl.class.getName());

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private SubtaskRepository subtaskRepository;
    
    @Autowired
    private TaskDependencyRepository taskDependencyRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskDTO createTask(CreateTaskRequest request) {
        logger.info("Creating task for project: " + request.getProjectId());
        
        // 1. Validate project exists
        if (!projectRepository.existsById(request.getProjectId())) {
            throw new ResourceNotFoundException("Project not found with id: " + request.getProjectId());
        }
        
        // 2. Validate reporter exists
        if (!userRepository.existsById(request.getReporterId())) {
            throw new ResourceNotFoundException("Reporter not found with id: " + request.getReporterId());
        }
        
        // 3. Validate assignee exists if provided
        if (request.getAssigneeId() != null && !userRepository.existsById(request.getAssigneeId())) {
            throw new ResourceNotFoundException("Assignee not found with id: " + request.getAssigneeId());
        }
        
        // 4. Create task entity
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
        task.setLoggedHours(0.0);
        
        if (request.getLabels() != null) {
            task.setLabels(request.getLabels());
        }
        
        // 5. Save to database
        Task savedTask = taskRepository.save(task);
        
        // 6. Create subtasks if provided
        if (request.getSubtasks() != null && !request.getSubtasks().isEmpty()) {
            for (CreateTaskRequest.SubtaskRequest subtaskRequest : request.getSubtasks()) {
                CreateSubtaskRequest createSubtaskRequest = new CreateSubtaskRequest();
                createSubtaskRequest.setTitle(subtaskRequest.getTitle());
                createSubtaskRequest.setAssigneeId(subtaskRequest.getAssigneeId());
                createSubtask(savedTask.getId(), createSubtaskRequest);
            }
        }
        
        // 7. Create dependencies if provided
        if (request.getDependencies() != null && !request.getDependencies().isEmpty()) {
            for (Long dependencyTaskId : request.getDependencies()) {
                Task dependencyTask = taskRepository.findById(dependencyTaskId).orElse(null);
                if (dependencyTask != null) {
                    TaskDependency dependency = new TaskDependency();
                    dependency.setDependentTask(savedTask);
                    dependency.setDependencyTask(dependencyTask);
                    dependency.setDependencyType("FINISH_TO_START");
                    taskDependencyRepository.save(dependency);
                }
            }
        }
        
        return convertToTaskDTO(savedTask);
    }

    @Override
    public SubtaskDTO createSubtask(Long taskId, CreateSubtaskRequest request) {
        logger.info("Creating subtask for task: " + taskId);
        
        // 1. Validate task exists
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        
        // 2. Validate assignee exists if provided
        if (request.getAssigneeId() != null && !userRepository.existsById(request.getAssigneeId())) {
            throw new ResourceNotFoundException("Assignee not found with id: " + request.getAssigneeId());
        }
        
        // 3. Create subtask entity
        Subtask subtask = new Subtask();
        subtask.setTask(task);
        subtask.setTitle(request.getTitle());
        subtask.setStatus(TaskStatus.TODO); // Default status
        subtask.setAssigneeId(request.getAssigneeId());
        
        // 4. Save to database
        Subtask savedSubtask = subtaskRepository.save(subtask);
        
        return convertToSubtaskDTO(savedSubtask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        logger.info("Fetching task by id: " + id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        return convertToTaskDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDetailDTO getTaskDetails(Long id) {
        logger.info("Fetching task details for id: " + id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
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
        
        // Convert subtasks
        List<SubtaskDTO> subtasks = task.getSubtasks().stream()
                .map(this::convertToSubtaskDTO)
                .collect(Collectors.toList());
        taskDetailDTO.setSubtasks(subtasks);
        
        // Convert comments to CommentDTO (not TaskCommentDTO)
        List<CommentDTO> comments = task.getComments().stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());
        taskDetailDTO.setComments(comments);
        
        // For now, we'll set attachments as empty list since there's no attachment entity
        taskDetailDTO.setAttachments(new ArrayList<>());
        
        return taskDetailDTO;
    }
    
    // Helper method to convert Task entity to TaskDTO
    private TaskDTO convertToTaskDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setProjectId(task.getProjectId());
        dto.setAssigneeId(task.getAssigneeId());
        dto.setDueDate(task.getDueDate());
        dto.setEstimatedHours(task.getEstimatedHours());
        dto.setLoggedHours(task.getLoggedHours());
        dto.setProgress(task.getProgress());
        dto.setLabels(task.getLabels());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
    
    // Helper method to convert Subtask entity to SubtaskDTO
    private SubtaskDTO convertToSubtaskDTO(Subtask subtask) {
        SubtaskDTO dto = new SubtaskDTO();
        dto.setId(subtask.getId());
        dto.setTaskId(subtask.getTask().getId());
        dto.setTitle(subtask.getTitle());
        dto.setStatus(subtask.getStatus());
        dto.setCompleted(subtask.getStatus() == TaskStatus.DONE);
        dto.setCompletedAt(subtask.getCompletedAt());
        dto.setCreatedAt(subtask.getCreatedAt());
        dto.setUpdatedAt(subtask.getUpdatedAt());
        return dto;
    }
    
    // Helper method to convert TaskComment entity to CommentDTO
    private CommentDTO convertToCommentDTO(com.aipm.ai_project_management.modules.tasks.entity.TaskComment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setTaskId(comment.getTask().getId());
        dto.setUserId(comment.getAuthorId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> getTasksByProjectId(Long projectId, Map<String, String> filters, Pageable pageable) {
        logger.info("Fetching tasks for project: " + projectId + " with filters: " + filters);
        
        // Validate project exists
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        
        Page<Task> tasksPage;
        
        // Apply filters if provided
        if (filters != null && filters.containsKey("status")) {
            TaskStatus status = TaskStatus.valueOf(filters.get("status").toUpperCase());
            tasksPage = taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        } else {
            tasksPage = taskRepository.findByProjectId(projectId, pageable);
        }
        
        List<TaskDTO> taskDTOs = tasksPage.getContent().stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(taskDTOs, pageable, tasksPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskBoardDTO getTaskBoard(Long projectId, String view) {
        logger.info("Fetching task board for project: " + projectId + " with view: " + view);
        
        // TODO: Implement task board logic
        TaskBoardDTO taskBoard = new TaskBoardDTO();
        taskBoard.setProjectId(projectId);
        taskBoard.setView(view);
        taskBoard.setColumns(new ArrayList<>());
        
        return taskBoard;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> getTasksAssignedToUser(Long userId, Map<String, String> filters, Pageable pageable) {
        logger.info("Fetching tasks assigned to user: " + userId + " with filters: " + filters);
        
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        Page<Task> tasksPage;
        
        // Apply filters if provided
        if (filters != null && filters.containsKey("status")) {
            TaskStatus status = TaskStatus.valueOf(filters.get("status").toUpperCase());
            tasksPage = taskRepository.findByAssigneeIdAndStatus(userId, status, pageable);
        } else {
            tasksPage = taskRepository.findByAssigneeId(userId, pageable);
        }
        
        List<TaskDTO> taskDTOs = tasksPage.getContent().stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(taskDTOs, pageable, tasksPage.getTotalElements());
    }

    @Override
    public TaskDTO updateTask(Long id, UpdateTaskRequest request) {
        logger.info("Updating task: " + id);
        
        // 1. Find existing task
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        // 2. Update fields if provided
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
            // Validate assignee exists
            if (!userRepository.existsById(request.getAssigneeId())) {
                throw new ResourceNotFoundException("Assignee not found with id: " + request.getAssigneeId());
            }
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
        
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public TaskDTO updateTaskStatus(Long id, TaskStatus status) {
        logger.info("Updating task status: " + id + " to " + status);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        task.setStatus(status);
        
        // Auto-update progress based on status
        if (status == TaskStatus.DONE) {
            task.setProgress(100);
        } else if (status == TaskStatus.IN_PROGRESS && task.getProgress() == 0) {
            task.setProgress(10); // Set to 10% when starting work
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public TaskDTO assignTask(Long taskId, Long userId) {
        logger.info("Assigning task: " + taskId + " to user: " + userId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        task.setAssigneeId(userId);
        Task updatedTask = taskRepository.save(task);
        
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public TaskDTO updateTaskProgress(Long taskId, Integer progress) {
        logger.info("Updating task progress: " + taskId + " to " + progress + "%");
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        
        task.setProgress(progress);
        
        // Auto-update status based on progress
        if (progress == 100 && task.getStatus() != TaskStatus.DONE) {
            task.setStatus(TaskStatus.DONE);
        } else if (progress > 0 && progress < 100 && task.getStatus() == TaskStatus.TODO) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToTaskDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        logger.info("Deleting task: " + id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        // The cascade settings in the entity will handle deletion of subtasks, comments, etc.
        taskRepository.delete(task);
    }

    @Override
    public List<TaskDTO> bulkUpdateTaskStatus(List<Long> taskIds, TaskStatus status) {
        logger.info("Bulk updating " + taskIds.size() + " tasks to status: " + status);
        
        List<TaskDTO> updatedTasks = new ArrayList<>();
        
        for (Long taskId : taskIds) {
            try {
                TaskDTO task = updateTaskStatus(taskId, status);
                updatedTasks.add(task);
            } catch (ResourceNotFoundException e) {
                logger.warning("Task not found during bulk update: " + taskId);
                // Continue with other tasks
            }
        }
        
        return updatedTasks;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> searchTasks(Long projectId, String searchTerm, Pageable pageable) {
        logger.info("Searching tasks in project: " + projectId + " with term: " + searchTerm);
        
        // Validate project exists
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        
        Page<Task> tasksPage = taskRepository.searchByProjectId(projectId, searchTerm, pageable);
        
        List<TaskDTO> taskDTOs = tasksPage.getContent().stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(taskDTOs, pageable, tasksPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getOverdueTasks() {
        logger.info("Fetching overdue tasks");
        
        LocalDateTime now = LocalDateTime.now();
        List<TaskStatus> completedStatuses = Arrays.asList(TaskStatus.DONE, TaskStatus.CANCELLED);
        
        List<Task> overdueTasks = taskRepository.findOverdueTasks(now, completedStatuses);
        
        return overdueTasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getUpcomingDeadlines(Integer days) {
        logger.info("Fetching tasks with deadlines in next " + days + " days");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(days);
        
        // We'll need to add this method to the repository
        // For now, let's use a simple approach
        List<Task> allTasks = taskRepository.findAll();
        List<Task> upcomingTasks = allTasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> task.getDueDate().isAfter(now) && task.getDueDate().isBefore(futureDate))
                .filter(task -> task.getStatus() != TaskStatus.DONE && task.getStatus() != TaskStatus.CANCELLED)
                .collect(Collectors.toList());
        
        return upcomingTasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }
}
