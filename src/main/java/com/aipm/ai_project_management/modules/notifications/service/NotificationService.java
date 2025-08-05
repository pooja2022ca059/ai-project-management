package com.aipm.ai_project_management.modules.notifications.service;

import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    
    // Create notifications
    NotificationEntity createNotification(NotificationEntity notification);
    NotificationEntity createNotification(Long recipientId, String title, String message, 
                                        NotificationEntity.NotificationType type);
    NotificationEntity createNotification(Long recipientId, String title, String message, 
                                        NotificationEntity.NotificationType type,
                                        NotificationEntity.NotificationPriority priority);
    
    // Send bulk notifications
    void sendNotificationToUsers(List<Long> userIds, String title, String message, 
                               NotificationEntity.NotificationType type);
    void sendNotificationToProjectMembers(Long projectId, String title, String message, 
                                        NotificationEntity.NotificationType type);
    
    // Get notifications
    Page<NotificationEntity> getUserNotifications(Long userId, Pageable pageable);
    List<NotificationEntity> getUnreadNotifications(Long userId);
    long getUnreadNotificationCount(Long userId);
    NotificationEntity getNotificationById(Long id);
    
    // Mark as read
    NotificationEntity markAsRead(Long notificationId, Long userId);
    void markAllAsRead(Long userId);
    
    // Delete notifications
    void deleteNotification(Long notificationId, Long userId);
    void deleteAllNotifications(Long userId);
    void deleteOldNotifications(int daysOld);
    
    // Real-time notifications
    void sendRealTimeNotification(Long userId, NotificationEntity notification);
    void broadcastSystemNotification(String title, String message);
    
    // Specific notification types
    void notifyTaskAssignment(Long taskId, Long assigneeId, Long assignerId);
    void notifyTaskCompletion(Long taskId, Long completerId, List<Long> stakeholderIds);
    void notifyDeadlineApproaching(Long taskId, Long assigneeId, int daysUntilDeadline);
    void notifyProjectUpdate(Long projectId, String updateMessage, List<Long> memberIds);
    void notifyMention(Long mentionedUserId, Long mentionerUserId, String context, String entityType, Long entityId);
    void notifyFileUpload(Long uploaderId, String fileName, Long projectId, List<Long> memberIds);
}