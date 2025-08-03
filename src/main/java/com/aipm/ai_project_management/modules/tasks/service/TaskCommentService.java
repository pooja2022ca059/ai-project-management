package com.aipm.ai_project_management.modules.tasks.service;

import com.aipm.ai_project_management.modules.tasks.dto.TaskCommentDTO;
import com.aipm.ai_project_management.modules.tasks.dto.CreateCommentRequest;

import java.util.List;

public interface TaskCommentService {
    
    /**
     * Add a new comment to a task
     * 
     * @param request The comment request containing content and metadata
     * @return The created comment
     */
    TaskCommentDTO addComment(CreateCommentRequest request);
    
    /**
     * Get all comments for a specific task
     * 
     * @param taskId The ID of the task
     * @return List of comments for the task
     */
    List<TaskCommentDTO> getCommentsByTaskId(Long taskId);
    
    /**
     * Reply to an existing comment
     * 
     * @param parentCommentId The ID of the parent comment
     * @param request The reply comment request
     * @return The created reply comment
     */
    TaskCommentDTO replyToComment(Long parentCommentId, CreateCommentRequest request);
    
    /**
     * Update an existing comment's content
     * 
     * @param commentId The ID of the comment to update
     * @param content The new content
     * @return The updated comment
     */
    TaskCommentDTO updateComment(Long commentId, String content);
    
    /**
     * Delete a comment
     * 
     * @param commentId The ID of the comment to delete
     * @param userId The ID of the user performing the delete (for permission check)
     */
    void deleteComment(Long commentId, Long userId);
}