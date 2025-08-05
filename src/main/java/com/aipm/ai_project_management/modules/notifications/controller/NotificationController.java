package com.aipm.ai_project_management.modules.notifications.controller;

import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import com.aipm.ai_project_management.modules.notifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Notification management and real-time messaging")
public class NotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    @Operation(summary = "Get user notifications with pagination")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<NotificationEntity>> getUserNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Fetching notifications for user: {}", userId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<NotificationEntity> notifications = notificationService.getUserNotifications(userId, pageable);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NotificationEntity>> getUnreadNotifications(Authentication authentication) {
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Fetching unread notifications for user: {}", userId);
        List<NotificationEntity> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread/count")
    @Operation(summary = "Get unread notification count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Long>> getUnreadNotificationCount(Authentication authentication) {
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Fetching unread notification count for user: {}", userId);
        long count = notificationService.getUnreadNotificationCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NotificationEntity> getNotificationById(@PathVariable Long id) {
        logger.info("Fetching notification with ID: {}", id);
        NotificationEntity notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
    
    @PostMapping("/{id}/mark-read")
    @Operation(summary = "Mark notification as read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NotificationEntity> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Marking notification {} as read for user: {}", id, userId);
        NotificationEntity notification = notificationService.markAsRead(id, userId);
        return ResponseEntity.ok(notification);
    }
    
    @PostMapping("/mark-all-read")
    @Operation(summary = "Mark all notifications as read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Marking all notifications as read for user: {}", userId);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a notification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Deleting notification {} for user: {}", id, userId);
        notificationService.deleteNotification(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/all")
    @Operation(summary = "Delete all notifications")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteAllNotifications(Authentication authentication) {
        Long userId = 1L; // Extract from authentication in real implementation
        
        logger.info("Deleting all notifications for user: {}", userId);
        notificationService.deleteAllNotifications(userId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/send-bulk")
    @Operation(summary = "Send bulk notifications")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Map<String, String>> sendBulkNotifications(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        logger.info("Sending bulk notifications by user: {}", authentication.getName());
        
        @SuppressWarnings("unchecked")
        List<Long> userIds = (List<Long>) request.get("userIds");
        String title = (String) request.get("title");
        String message = (String) request.get("message");
        String typeStr = (String) request.get("type");
        
        NotificationEntity.NotificationType type = NotificationEntity.NotificationType.valueOf(typeStr);
        
        notificationService.sendNotificationToUsers(userIds, title, message, type);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Bulk notifications sent successfully"));
    }
    
    @PostMapping("/send-project-notification/{projectId}")
    @Operation(summary = "Send notification to project members")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> sendProjectNotification(
            @PathVariable Long projectId,
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        
        logger.info("Sending project notification for project {} by user: {}", projectId, authentication.getName());
        
        String title = request.get("title");
        String message = request.get("message");
        String typeStr = request.get("type");
        
        NotificationEntity.NotificationType type = NotificationEntity.NotificationType.valueOf(typeStr);
        
        notificationService.sendNotificationToProjectMembers(projectId, title, message, type);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Project notification sent successfully"));
    }
    
    @PostMapping("/broadcast")
    @Operation(summary = "Broadcast system notification")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> broadcastSystemNotification(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        
        logger.info("Broadcasting system notification by admin: {}", authentication.getName());
        
        String title = request.get("title");
        String message = request.get("message");
        
        notificationService.broadcastSystemNotification(title, message);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "System notification broadcasted successfully"));
    }
    
    @PostMapping("/cleanup/old")
    @Operation(summary = "Cleanup old notifications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> cleanupOldNotifications(
            @RequestParam(defaultValue = "30") int daysOld,
            Authentication authentication) {
        
        logger.info("Cleaning up notifications older than {} days by admin: {}", daysOld, authentication.getName());
        
        notificationService.deleteOldNotifications(daysOld);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Old notifications cleaned up successfully"));
    }
    
    // Specific notification trigger endpoints
    @PostMapping("/task-assignment")
    @Operation(summary = "Send task assignment notification")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> notifyTaskAssignment(
            @RequestBody Map<String, Long> request,
            Authentication authentication) {
        
        Long taskId = request.get("taskId");
        Long assigneeId = request.get("assigneeId");
        Long assignerId = 1L; // Extract from authentication in real implementation
        
        logger.info("Sending task assignment notification for task {} to user {} by user: {}", 
                   taskId, assigneeId, assignerId);
        
        notificationService.notifyTaskAssignment(taskId, assigneeId, assignerId);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Task assignment notification sent"));
    }
    
    @PostMapping("/task-completion")
    @Operation(summary = "Send task completion notification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> notifyTaskCompletion(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        Long taskId = ((Number) request.get("taskId")).longValue();
        Long completerId = 1L; // Extract from authentication in real implementation
        
        @SuppressWarnings("unchecked")
        List<Long> stakeholderIds = (List<Long>) request.get("stakeholderIds");
        
        logger.info("Sending task completion notification for task {} by user: {}", taskId, completerId);
        
        notificationService.notifyTaskCompletion(taskId, completerId, stakeholderIds);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Task completion notification sent"));
    }
    
    @PostMapping("/deadline-reminder")
    @Operation(summary = "Send deadline reminder notification")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<Map<String, String>> notifyDeadlineApproaching(
            @RequestBody Map<String, Object> request) {
        
        Long taskId = ((Number) request.get("taskId")).longValue();
        Long assigneeId = ((Number) request.get("assigneeId")).longValue();
        Integer daysUntilDeadline = ((Number) request.get("daysUntilDeadline")).intValue();
        
        logger.info("Sending deadline reminder for task {} to user {} ({} days remaining)", 
                   taskId, assigneeId, daysUntilDeadline);
        
        notificationService.notifyDeadlineApproaching(taskId, assigneeId, daysUntilDeadline);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Deadline reminder sent"));
    }
    
    @PostMapping("/mention")
    @Operation(summary = "Send mention notification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> notifyMention(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        Long mentionedUserId = ((Number) request.get("mentionedUserId")).longValue();
        Long mentionerUserId = 1L; // Extract from authentication in real implementation
        String context = (String) request.get("context");
        String entityType = (String) request.get("entityType");
        Long entityId = ((Number) request.get("entityId")).longValue();
        
        logger.info("Sending mention notification to user {} by user: {}", mentionedUserId, mentionerUserId);
        
        notificationService.notifyMention(mentionedUserId, mentionerUserId, context, entityType, entityId);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Mention notification sent"));
    }
    
    @PostMapping("/file-upload")
    @Operation(summary = "Send file upload notification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> notifyFileUpload(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        Long uploaderId = 1L; // Extract from authentication in real implementation
        String fileName = (String) request.get("fileName");
        Long projectId = ((Number) request.get("projectId")).longValue();
        
        @SuppressWarnings("unchecked")
        List<Long> memberIds = (List<Long>) request.get("memberIds");
        
        logger.info("Sending file upload notification for file {} by user: {}", fileName, uploaderId);
        
        notificationService.notifyFileUpload(uploaderId, fileName, projectId, memberIds);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "File upload notification sent"));
    }
}

// WebSocket message handling for real-time notifications
@Controller
class NotificationWebSocketController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationWebSocketController.class);
    
    @MessageMapping("/notifications/subscribe")
    @SendTo("/topic/notifications")
    public String subscribeToNotifications(String message) {
        logger.info("User subscribed to notifications: {}", message);
        return "Subscribed to notifications";
    }
    
    @MessageMapping("/notifications/unsubscribe")
    @SendTo("/topic/notifications")
    public String unsubscribeFromNotifications(String message) {
        logger.info("User unsubscribed from notifications: {}", message);
        return "Unsubscribed from notifications";
    }
}