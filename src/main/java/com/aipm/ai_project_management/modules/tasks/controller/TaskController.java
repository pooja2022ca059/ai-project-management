package com.aipm.ai_project_management.modules.tasks.controller;

import com.aipm.ai_project_management.common.enums.TaskStatus;
import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.common.response.PageResponse;
import com.aipm.ai_project_management.modules.tasks.dto.*;
import com.aipm.ai_project_management.modules.tasks.service.TaskService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/projects/{projectId}/tasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<PageResponse<TaskDTO>>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) Map<String, String> filters,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<TaskDTO> tasks = taskService.getTasksByProjectId(projectId, filters, pageable);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(tasks)));
    }

    @GetMapping("/projects/{projectId}/tasks/board")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskBoardDTO>> getTaskBoard(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "kanban") String view) {
        
        TaskBoardDTO board = taskService.getTaskBoard(projectId, view);
        return ResponseEntity.ok(ApiResponse.success(board));
    }

    @GetMapping("/tasks/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskDetailDTO>> getTaskById(@PathVariable Long id) {
        TaskDetailDTO task = taskService.getTaskDetails(id);
        return ResponseEntity.ok(ApiResponse.success(task));
    }

    @PostMapping("/projects/{projectId}/tasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request) {
        
        request.setProjectId(projectId); // Ensure projectId is set
        TaskDTO createdTask = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdTask));
    }

    @PutMapping("/tasks/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        
        TaskDTO updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedTask));
    }

    @PatchMapping("/tasks/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {
        
        TaskDTO updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updatedTask));
    }

    @PatchMapping("/tasks/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<TaskDTO>> assignTask(
            @PathVariable Long id,
            @RequestParam Long userId) {
        
        TaskDTO updatedTask = taskService.assignTask(id, userId);
        return ResponseEntity.ok(ApiResponse.success(updatedTask));
    }

    @PatchMapping("/tasks/{id}/progress")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTaskProgress(
            @PathVariable Long id,
            @RequestParam Integer progress) {
        
        TaskDTO updatedTask = taskService.updateTaskProgress(id, progress);
        return ResponseEntity.ok(ApiResponse.success(updatedTask));
    }

    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Task deleted successfully"));
    }
}
