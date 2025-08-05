package com.aipm.ai_project_management.modules.notifications.service;

import com.aipm.ai_project_management.modules.notifications.dto.NotificationDTO;
import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    
    /**
     * Create a new notification
     */
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    
    /**
     * Send notification to user
     */
    NotificationDTO sendNotification(Long recipientId, String title, String message, 
                                   NotificationEntity.NotificationType type);
    
    /**
     * Send notification with additional details
     */
    NotificationDTO sendNotification(Long recipientId, Long senderId, String title, String message, 
                                   NotificationEntity.NotificationType type, 
                                   NotificationEntity.NotificationPriority priority,
                                   String relatedEntityType, Long relatedEntityId, String actionUrl);
    
    /**
     * Get notifications for a user
     */
    Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable);
    
    /**
     * Get unread notifications for a user
     */
    Page<NotificationDTO> getUnreadNotifications(Long userId, Pageable pageable);
    
    /**
     * Get notifications by type for a user
     */
    Page<NotificationDTO> getNotificationsByType(Long userId, NotificationEntity.NotificationType type, Pageable pageable);
    
    /**
     * Mark notification as read
     */
    NotificationDTO markAsRead(Long notificationId);
    
    /**
     * Mark all notifications as read for a user
     */
    void markAllAsRead(Long userId);
    
    /**
     * Delete notification
     */
    void deleteNotification(Long notificationId);
    
    /**
     * Delete all notifications for a user
     */
    void deleteAllUserNotifications(Long userId);
    
    /**
     * Get notification by ID
     */
    NotificationDTO getNotificationById(Long notificationId);
    
    /**
     * Get unread count for a user
     */
    long getUnreadCount(Long userId);
    
    /**
     * Send bulk notifications to multiple users
     */
    List<NotificationDTO> sendBulkNotifications(List<Long> recipientIds, String title, String message, 
                                              NotificationEntity.NotificationType type);
    
    /**
     * Send system notification to all users
     */
    void sendSystemNotification(String title, String message);
    
    /**
     * Clean up old read notifications
     */
    void cleanupOldNotifications(int daysOld);
    
    /**
     * Get notification statistics for a user
     */
    NotificationStats getUserNotificationStats(Long userId);
    
    /**
     * Inner class for notification statistics
     */
    class NotificationStats {
        private long totalNotifications;
        private long unreadCount;
        private long readCount;
        
        public NotificationStats(long totalNotifications, long unreadCount, long readCount) {
            this.totalNotifications = totalNotifications;
            this.unreadCount = unreadCount;
            this.readCount = readCount;
        }
        
        // Getters
        public long getTotalNotifications() { return totalNotifications; }
        public long getUnreadCount() { return unreadCount; }
        public long getReadCount() { return readCount; }
    }
}