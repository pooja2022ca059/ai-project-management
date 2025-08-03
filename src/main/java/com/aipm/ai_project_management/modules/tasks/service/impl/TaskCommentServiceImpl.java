package com.aipm.ai_project_management.modules.tasks.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.common.exceptions.UnauthorizedException;
import com.aipm.ai_project_management.modules.tasks.dto.CreateCommentRequest;
import com.aipm.ai_project_management.modules.tasks.dto.TaskCommentDTO;
import com.aipm.ai_project_management.modules.tasks.dto.UserSummaryDTO;
import com.aipm.ai_project_management.modules.tasks.entity.Task;
import com.aipm.ai_project_management.modules.tasks.entity.TaskComment;
import com.aipm.ai_project_management.modules.tasks.repository.TaskCommentRepository;
import com.aipm.ai_project_management.modules.tasks.repository.TaskRepository;
import com.aipm.ai_project_management.modules.tasks.service.TaskCommentService;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskCommentServiceImpl implements TaskCommentService {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public TaskCommentServiceImpl(
            TaskCommentRepository taskCommentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TaskCommentDTO addComment(CreateCommentRequest request) {
        // Check if task exists
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));
        
        // Check if user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        
        // Create comment entity
        TaskComment comment = new TaskComment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setAuthorId(user.getId());
        comment.setCreatedAt(LocalDateTime.now());
        
        // Handle mentions
        if (request.getMentions() != null && !request.getMentions().isEmpty()) {
            comment.setMentions(request.getMentions());
        }
        
        // Save comment
        TaskComment savedComment = taskCommentRepository.save(comment);
        
        // Convert to DTO and return
        return convertToDTO(savedComment);
    }

    @Override
    public List<TaskCommentDTO> getCommentsByTaskId(Long taskId) {
        // Check if task exists
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        
        // Get root comments (no parent)
        List<TaskComment> rootComments = taskCommentRepository.findByTaskIdAndParentIdIsNullOrderByCreatedAtAsc(taskId);
        
        // Convert to DTOs with replies
        return rootComments.stream()
                .map(this::convertToDTOWithReplies)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskCommentDTO replyToComment(Long parentCommentId, CreateCommentRequest request) {
        // Check if parent comment exists
        TaskComment parentComment = taskCommentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + parentCommentId));
        
        // Check if user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        
        // Create reply comment
        TaskComment reply = new TaskComment();
        reply.setContent(request.getContent());
        reply.setTask(parentComment.getTask());
        reply.setAuthorId(user.getId());
        reply.setParentId(parentCommentId);
        reply.setCreatedAt(LocalDateTime.now());
        
        // Handle mentions
        if (request.getMentions() != null && !request.getMentions().isEmpty()) {
            reply.setMentions(request.getMentions());
        }
        
        // Save reply
        TaskComment savedReply = taskCommentRepository.save(reply);
        
        // Convert to DTO and return
        return convertToDTO(savedReply);
    }

    @Override
    @Transactional
    public TaskCommentDTO updateComment(Long commentId, String content) {
        // Check if comment exists
        TaskComment comment = taskCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        // Update content
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        
        // Save updated comment
        TaskComment updatedComment = taskCommentRepository.save(comment);
        
        // Convert to DTO and return
        return convertToDTO(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        // Check if comment exists
        TaskComment comment = taskCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        // Check if user has permission to delete (author or admin)
        if (!comment.getAuthorId().equals(userId)) {
            // Check if user is admin or project manager
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            
            // Check if user role is ADMIN or PROJECT_MANAGER
            UserRole role = user.getRole();
            boolean isAdmin = role == UserRole.ADMIN || role == UserRole.PROJECT_MANAGER;
            
            if (!isAdmin) {
                throw new UnauthorizedException("You don't have permission to delete this comment");
            }
        }
        
        // Delete comment
        taskCommentRepository.delete(comment);
    }
    
    /**
     * Helper method to convert a TaskComment entity to DTO
     */
    private TaskCommentDTO convertToDTO(TaskComment comment) {
        TaskCommentDTO dto = new TaskCommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setTaskId(comment.getTask().getId());
        dto.setParentId(comment.getParentId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        
        // Set author information
        User author = userRepository.findById(comment.getAuthorId())
                .orElse(new User()); // Fallback to empty user if not found
        
        UserSummaryDTO authorDto = new UserSummaryDTO();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setEmail(author.getEmail());
        authorDto.setAvatar(author.getAvatar());
        
        // Convert UserRole enum to String before setting it
        if (author.getRole() != null) {
            authorDto.setRole(author.getRole().toString());
        }
        
        dto.setAuthor(authorDto);
        
        // Set mentions if any
        if (comment.getMentions() != null && !comment.getMentions().isEmpty()) {
            List<UserSummaryDTO> mentionsDto = new ArrayList<>();
            for (Long mentionedUserId : comment.getMentions()) {
                User mentionedUser = userRepository.findById(mentionedUserId)
                        .orElse(new User());
                
                UserSummaryDTO mentionDto = new UserSummaryDTO();
                mentionDto.setId(mentionedUser.getId());
                mentionDto.setName(mentionedUser.getName());
                mentionDto.setAvatar(mentionedUser.getAvatar());
                mentionsDto.add(mentionDto);
            }
            dto.setMentions(mentionsDto);
        }
        
        return dto;
    }
    
    /**
     * Helper method to convert a TaskComment entity to DTO with replies
     */
    private TaskCommentDTO convertToDTOWithReplies(TaskComment comment) {
        TaskCommentDTO dto = convertToDTO(comment);
        
        // Add replies recursively
        List<TaskComment> replies = taskCommentRepository.findByParentIdOrderByCreatedAtAsc(comment.getId());
        if (replies != null && !replies.isEmpty()) {
            List<TaskCommentDTO> repliesDto = replies.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            dto.setReplies(repliesDto);
        }
        
        return dto;
    }
}