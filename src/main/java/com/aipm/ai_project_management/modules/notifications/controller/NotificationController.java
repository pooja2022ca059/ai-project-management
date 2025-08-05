package com.aipm.ai_project_management.modules.notifications.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.common.response.SuccessResponse;
import com.aipm.ai_project_management.modules.shared.entity.NotificationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for managing notifications in the system.
 * Provides endpoints for retrieving, marking as read, and managing user notifications.
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Notification management endpoints")
public class NotificationController {

    @Operation(summary = "Get user notifications", description = "Retrieve notifications for the current user")
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<NotificationDTO>>> getMyNotifications(
            @RequestParam(defaultValue = "false") boolean unreadOnly,
            Pageable pageable) {
        
        // For now, return empty list - will be implemented with real notification service
        List<NotificationDTO> notifications = new ArrayList<>();
        Page<NotificationDTO> notificationPage = new PageImpl<>(notifications, pageable, 0);
        
        return ResponseEntity.ok(ApiResponse.success(notificationPage));
    }

    @Operation(summary = "Get notification count", description = "Get count of unread notifications for current user")
    @GetMapping("/count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<NotificationCountDTO>> getNotificationCount() {
        
        // For now, return zero count - will be implemented with real notification service
        NotificationCountDTO count = new NotificationCountDTO();
        count.setUnreadCount(0);
        count.setTotalCount(0);
        
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @Operation(summary = "Mark notification as read", description = "Mark a specific notification as read")
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long notificationId) {
        
        // For now, return success - will be implemented with real notification service
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", null));
    }

    @Operation(summary = "Mark all notifications as read", description = "Mark all notifications as read for current user")
    @PutMapping("/mark-all-read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        
        // For now, return success - will be implemented with real notification service
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }

    @Operation(summary = "Delete notification", description = "Delete a specific notification")
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long notificationId) {
        
        // For now, return success - will be implemented with real notification service
        return ResponseEntity.ok(ApiResponse.success("Notification deleted", null));
    }

    // DTOs
    public static class NotificationDTO {
        private Long id;
        private String title;
        private String message;
        private NotificationEntity.NotificationType type;
        private NotificationEntity.NotificationPriority priority;
        private String entityType;
        private Long entityId;
        private Boolean isRead;
        private LocalDateTime readAt;
        private String actionUrl;
        private LocalDateTime createdAt;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public NotificationEntity.NotificationType getType() {
            return type;
        }

        public void setType(NotificationEntity.NotificationType type) {
            this.type = type;
        }

        public NotificationEntity.NotificationPriority getPriority() {
            return priority;
        }

        public void setPriority(NotificationEntity.NotificationPriority priority) {
            this.priority = priority;
        }

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public Long getEntityId() {
            return entityId;
        }

        public void setEntityId(Long entityId) {
            this.entityId = entityId;
        }

        public Boolean getIsRead() {
            return isRead;
        }

        public void setIsRead(Boolean isRead) {
            this.isRead = isRead;
        }

        public LocalDateTime getReadAt() {
            return readAt;
        }

        public void setReadAt(LocalDateTime readAt) {
            this.readAt = readAt;
        }

        public String getActionUrl() {
            return actionUrl;
        }

        public void setActionUrl(String actionUrl) {
            this.actionUrl = actionUrl;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }

    public static class NotificationCountDTO {
        private Integer unreadCount;
        private Integer totalCount;

        public Integer getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(Integer unreadCount) {
            this.unreadCount = unreadCount;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }
    }
}