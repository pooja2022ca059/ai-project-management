package com.aipm.ai_project_management.modules.notifications.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.common.response.PageResponse;
import com.aipm.ai_project_management.modules.notifications.dto.NotificationDTO;
import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import com.aipm.ai_project_management.modules.notifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping
    @Operation(summary = "Create a new notification")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationDTO>> createNotification(
            @Valid @RequestBody NotificationDTO notificationDTO) {
        
        NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
        return ResponseEntity.ok(ApiResponse.success("Notification created successfully", createdNotification));
    }
    
    @PostMapping("/send")
    @Operation(summary = "Send a notification to a user")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationDTO>> sendNotification(
            @Parameter(description = "Recipient user ID") @RequestParam Long recipientId,
            @Parameter(description = "Notification title") @RequestParam String title,
            @Parameter(description = "Notification message") @RequestParam String message,
            @Parameter(description = "Notification type") @RequestParam NotificationEntity.NotificationType type,
            @Parameter(description = "Sender user ID") @RequestParam(required = false) Long senderId,
            @Parameter(description = "Priority") @RequestParam(required = false) NotificationEntity.NotificationPriority priority,
            @Parameter(description = "Related entity type") @RequestParam(required = false) String relatedEntityType,
            @Parameter(description = "Related entity ID") @RequestParam(required = false) Long relatedEntityId,
            @Parameter(description = "Action URL") @RequestParam(required = false) String actionUrl) {
        
        NotificationDTO notification = notificationService.sendNotification(
            recipientId, senderId, title, message, type, priority, 
            relatedEntityType, relatedEntityId, actionUrl);
        
        return ResponseEntity.ok(ApiResponse.success("Notification sent successfully", notification));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications for a user")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationDTO>>> getUserNotifications(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notifications = notificationService.getUserNotifications(userId, pageable);
        
        PageResponse<NotificationDTO> pageResponse = new PageResponse<>(
            notifications.getContent(),
            notifications.getNumber(),
            notifications.getSize(),
            notifications.getTotalElements(),
            notifications.getTotalPages(),
            notifications.isFirst(),
            notifications.isLast()
        );
        
        return ResponseEntity.ok(ApiResponse.success("User notifications retrieved successfully", pageResponse));
    }
    
    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications for a user")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationDTO>>> getUnreadNotifications(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId, pageable);
        
        PageResponse<NotificationDTO> pageResponse = new PageResponse<>(
            notifications.getContent(),
            notifications.getNumber(),
            notifications.getSize(),
            notifications.getTotalElements(),
            notifications.getTotalPages(),
            notifications.isFirst(),
            notifications.isLast()
        );
        
        return ResponseEntity.ok(ApiResponse.success("Unread notifications retrieved successfully", pageResponse));
    }
    
    @GetMapping("/user/{userId}/count/unread")
    @Operation(summary = "Get unread notification count for a user")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        long unreadCount = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponse.success("Unread count retrieved successfully", unreadCount));
    }
    
    @PutMapping("/{notificationId}/read")
    @Operation(summary = "Mark notification as read")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(
            @Parameter(description = "Notification ID") @PathVariable Long notificationId) {
        
        NotificationDTO notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", notification));
    }
    
    @PutMapping("/user/{userId}/read-all")
    @Operation(summary = "Mark all notifications as read for a user")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }
    
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete a notification")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @Parameter(description = "Notification ID") @PathVariable Long notificationId) {
        
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(ApiResponse.success("Notification deleted successfully", null));
    }
    
    @PostMapping("/bulk")
    @Operation(summary = "Send bulk notifications")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> sendBulkNotifications(
            @Parameter(description = "Recipient user IDs") @RequestParam List<Long> recipientIds,
            @Parameter(description = "Notification title") @RequestParam String title,
            @Parameter(description = "Notification message") @RequestParam String message,
            @Parameter(description = "Notification type") @RequestParam NotificationEntity.NotificationType type) {
        
        List<NotificationDTO> notifications = notificationService.sendBulkNotifications(recipientIds, title, message, type);
        return ResponseEntity.ok(ApiResponse.success("Bulk notifications sent successfully", notifications));
    }
    
    @PostMapping("/system")
    @Operation(summary = "Send system notification to all users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendSystemNotification(
            @Parameter(description = "Notification title") @RequestParam String title,
            @Parameter(description = "Notification message") @RequestParam String message) {
        
        notificationService.sendSystemNotification(title, message);
        return ResponseEntity.ok(ApiResponse.success("System notification sent to all users", null));
    }
    
    @GetMapping("/user/{userId}/stats")
    @Operation(summary = "Get notification statistics for a user")
    @PreAuthorize("hasRole('TEAM_MEMBER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationService.NotificationStats>> getUserNotificationStats(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        NotificationService.NotificationStats stats = notificationService.getUserNotificationStats(userId);
        return ResponseEntity.ok(ApiResponse.success("Notification statistics retrieved successfully", stats));
    }
    
    @DeleteMapping("/cleanup")
    @Operation(summary = "Clean up old notifications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> cleanupOldNotifications(
            @Parameter(description = "Days old") @RequestParam(defaultValue = "90") int daysOld) {
        
        notificationService.cleanupOldNotifications(daysOld);
        return ResponseEntity.ok(ApiResponse.success("Old notifications cleaned up successfully", null));
    }
}