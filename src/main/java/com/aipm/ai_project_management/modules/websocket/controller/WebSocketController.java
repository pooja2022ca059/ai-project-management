package com.aipm.ai_project_management.modules.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket controller for real-time messaging and updates.
 * Handles real-time notifications, task updates, and collaborative features.
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Handle task updates and broadcast to subscribers.
     */
    @MessageMapping("/task/update")
    @SendTo("/topic/tasks")
    public Map<String, Object> handleTaskUpdate(Map<String, Object> taskUpdate) {
        // Add timestamp to the update
        taskUpdate.put("timestamp", LocalDateTime.now());
        taskUpdate.put("type", "TASK_UPDATE");
        return taskUpdate;
    }

    /**
     * Handle project updates and broadcast to subscribers.
     */
    @MessageMapping("/project/update")
    @SendTo("/topic/projects")
    public Map<String, Object> handleProjectUpdate(Map<String, Object> projectUpdate) {
        projectUpdate.put("timestamp", LocalDateTime.now());
        projectUpdate.put("type", "PROJECT_UPDATE");
        return projectUpdate;
    }

    /**
     * Handle comment additions and broadcast to subscribers.
     */
    @MessageMapping("/comment/add")
    @SendTo("/topic/comments")
    public Map<String, Object> handleCommentAdd(Map<String, Object> comment) {
        comment.put("timestamp", LocalDateTime.now());
        comment.put("type", "COMMENT_ADDED");
        return comment;
    }

    /**
     * Send notification to specific user.
     */
    public void sendNotificationToUser(Long userId, Map<String, Object> notification) {
        notification.put("timestamp", LocalDateTime.now());
        notification.put("type", "NOTIFICATION");
        messagingTemplate.convertAndSendToUser(
            userId.toString(), 
            "/topic/notifications", 
            notification
        );
    }

    /**
     * Broadcast notification to all users.
     */
    public void broadcastNotification(Map<String, Object> notification) {
        notification.put("timestamp", LocalDateTime.now());
        notification.put("type", "BROADCAST_NOTIFICATION");
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    /**
     * Send live collaboration update.
     */
    public void sendCollaborationUpdate(String entityType, Long entityId, Map<String, Object> update) {
        update.put("timestamp", LocalDateTime.now());
        update.put("type", "COLLABORATION_UPDATE");
        update.put("entityType", entityType);
        update.put("entityId", entityId);
        messagingTemplate.convertAndSend("/topic/collaboration", update);
    }

    /**
     * Helper method to create a notification message.
     */
    public Map<String, Object> createNotification(String title, String message, String type, Long targetUserId) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("message", message);
        notification.put("notificationType", type);
        notification.put("targetUserId", targetUserId);
        notification.put("timestamp", LocalDateTime.now());
        return notification;
    }
}