package com.aipm.ai_project_management.modules.tasks.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.tasks.dto.CreateCommentRequest;
import com.aipm.ai_project_management.modules.tasks.dto.TaskCommentDTO;
import com.aipm.ai_project_management.modules.tasks.service.TaskCommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskCommentController {

    private final TaskCommentService commentService;
    
    @Autowired
    public TaskCommentController(TaskCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/tasks/{taskId}/comments")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskCommentDTO>> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request) {
        
        request.setTaskId(taskId);
        TaskCommentDTO comment = commentService.addComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(comment));
    }

    @GetMapping("/tasks/{taskId}/comments")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<List<TaskCommentDTO>>> getTaskComments(
            @PathVariable Long taskId) {
        
        List<TaskCommentDTO> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @PostMapping("/comments/{commentId}/replies")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskCommentDTO>> replyToComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CreateCommentRequest request) {
        
        TaskCommentDTO reply = commentService.replyToComment(commentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(reply));
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TaskCommentDTO>> updateComment(
            @PathVariable Long commentId,
            @RequestBody String content) {
        
        TaskCommentDTO updatedComment = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(ApiResponse.success(updatedComment));
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<String>> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully"));
    }
}