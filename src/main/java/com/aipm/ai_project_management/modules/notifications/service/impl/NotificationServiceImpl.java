package com.aipm.ai_project_management.modules.notifications.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import com.aipm.ai_project_management.modules.notifications.repository.NotificationRepository;
import com.aipm.ai_project_management.modules.notifications.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Override
    public NotificationEntity createNotification(NotificationEntity notification) {
        logger.info("Creating notification for user: {}", notification.getRecipientId());
        
        NotificationEntity savedNotification = notificationRepository.save(notification);
        
        // Send real-time notification
        sendRealTimeNotification(notification.getRecipientId(), savedNotification);
        
        logger.info("Created notification with ID: {}", savedNotification.getId());
        return savedNotification;
    }
    
    @Override
    public NotificationEntity createNotification(Long recipientId, String title, String message, 
                                               NotificationEntity.NotificationType type) {
        NotificationEntity notification = new NotificationEntity(recipientId, title, message, type);
        return createNotification(notification);
    }
    
    @Override
    public NotificationEntity createNotification(Long recipientId, String title, String message, 
                                               NotificationEntity.NotificationType type,
                                               NotificationEntity.NotificationPriority priority) {
        NotificationEntity notification = new NotificationEntity(recipientId, title, message, type);
        notification.setPriority(priority);
        return createNotification(notification);
    }
    
    @Override
    public void sendNotificationToUsers(List<Long> userIds, String title, String message, 
                                      NotificationEntity.NotificationType type) {
        logger.info("Sending bulk notification to {} users", userIds.size());
        
        for (Long userId : userIds) {
            try {
                createNotification(userId, title, message, type);
            } catch (Exception e) {
                logger.error("Failed to send notification to user {}: {}", userId, e.getMessage(), e);
            }
        }
    }
    
    @Override
    public void sendNotificationToProjectMembers(Long projectId, String title, String message, 
                                               NotificationEntity.NotificationType type) {
        logger.info("Sending notification to project {} members", projectId);
        
        // In a real implementation, you would fetch project members
        // For now, this is a placeholder
        List<Long> projectMemberIds = getProjectMemberIds(projectId);
        sendNotificationToUsers(projectMemberIds, title, message, type);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationEntity> getUserNotifications(Long userId, Pageable pageable) {
        logger.info("Fetching notifications for user: {}", userId);
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NotificationEntity> getUnreadNotifications(Long userId) {
        logger.info("Fetching unread notifications for user: {}", userId);
        return notificationRepository.findByRecipientIdAndIsReadFalse(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public NotificationEntity getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
    }
    
    @Override
    public NotificationEntity markAsRead(Long notificationId, Long userId) {
        logger.info("Marking notification {} as read for user: {}", notificationId, userId);
        
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        // Verify the notification belongs to the user
        if (!notification.getRecipientId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to the specified user");
        }
        
        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification = notificationRepository.save(notification);
        }
        
        return notification;
    }
    
    @Override
    public void markAllAsRead(Long userId) {
        logger.info("Marking all notifications as read for user: {}", userId);
        
        List<NotificationEntity> unreadNotifications = notificationRepository
                .findByRecipientIdAndIsReadFalse(userId);
        
        LocalDateTime now = LocalDateTime.now();
        for (NotificationEntity notification : unreadNotifications) {
            notification.setIsRead(true);
            notification.setReadAt(now);
        }
        
        notificationRepository.saveAll(unreadNotifications);
        logger.info("Marked {} notifications as read for user: {}", unreadNotifications.size(), userId);
    }
    
    @Override
    public void deleteNotification(Long notificationId, Long userId) {
        logger.info("Deleting notification {} for user: {}", notificationId, userId);
        
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        // Verify the notification belongs to the user
        if (!notification.getRecipientId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to the specified user");
        }
        
        notificationRepository.delete(notification);
        logger.info("Deleted notification: {}", notificationId);
    }
    
    @Override
    public void deleteAllNotifications(Long userId) {
        logger.info("Deleting all notifications for user: {}", userId);
        
        long deletedCount = notificationRepository.deleteByRecipientId(userId);
        logger.info("Deleted {} notifications for user: {}", deletedCount, userId);
    }
    
    @Override
    public void deleteOldNotifications(int daysOld) {
        logger.info("Deleting notifications older than {} days", daysOld);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        long deletedCount = notificationRepository.deleteByCreatedAtBefore(cutoffDate);
        
        logger.info("Deleted {} old notifications", deletedCount);
    }
    
    @Override
    public void sendRealTimeNotification(Long userId, NotificationEntity notification) {
        logger.info("Sending real-time notification to user: {}", userId);
        
        try {
            // Send to user-specific destination
            messagingTemplate.convertAndSendToUser(
                    userId.toString(), 
                    "/queue/notifications", 
                    notification
            );
            
            logger.info("Real-time notification sent to user: {}", userId);
        } catch (Exception e) {
            logger.error("Failed to send real-time notification to user {}: {}", userId, e.getMessage(), e);
        }
    }
    
    @Override
    public void broadcastSystemNotification(String title, String message) {
        logger.info("Broadcasting system notification: {}", title);
        
        try {
            NotificationEntity systemNotification = new NotificationEntity();
            systemNotification.setTitle(title);
            systemNotification.setMessage(message);
            systemNotification.setType(NotificationEntity.NotificationType.SYSTEM_ALERT);
            systemNotification.setPriority(NotificationEntity.NotificationPriority.HIGH);
            
            // Broadcast to all connected users
            messagingTemplate.convertAndSend("/topic/system-notifications", systemNotification);
            
            logger.info("System notification broadcasted successfully");
        } catch (Exception e) {
            logger.error("Failed to broadcast system notification: {}", e.getMessage(), e);
        }
    }
    
    // Specific notification methods
    @Override
    public void notifyTaskAssignment(Long taskId, Long assigneeId, Long assignerId) {
        logger.info("Notifying task assignment: task={}, assignee={}, assigner={}", taskId, assigneeId, assignerId);
        
        String title = "New Task Assigned";
        String message = "You have been assigned a new task.";
        
        NotificationEntity notification = new NotificationEntity(assigneeId, title, message, 
                NotificationEntity.NotificationType.TASK_ASSIGNED);
        notification.setSenderId(assignerId);
        notification.setRelatedEntityType("TASK");
        notification.setRelatedEntityId(taskId);
        notification.setActionUrl("/tasks/" + taskId);
        
        createNotification(notification);
    }
    
    @Override
    public void notifyTaskCompletion(Long taskId, Long completerId, List<Long> stakeholderIds) {
        logger.info("Notifying task completion: task={}, completer={}", taskId, completerId);
        
        String title = "Task Completed";
        String message = "A task has been marked as completed.";
        
        for (Long stakeholderId : stakeholderIds) {
            NotificationEntity notification = new NotificationEntity(stakeholderId, title, message, 
                    NotificationEntity.NotificationType.TASK_COMPLETED);
            notification.setSenderId(completerId);
            notification.setRelatedEntityType("TASK");
            notification.setRelatedEntityId(taskId);
            notification.setActionUrl("/tasks/" + taskId);
            
            createNotification(notification);
        }
    }
    
    @Override
    public void notifyDeadlineApproaching(Long taskId, Long assigneeId, int daysUntilDeadline) {
        logger.info("Notifying deadline approaching: task={}, assignee={}, days={}", taskId, assigneeId, daysUntilDeadline);
        
        String title = "Deadline Approaching";
        String message = String.format("Your task deadline is in %d day(s).", daysUntilDeadline);
        
        NotificationEntity notification = new NotificationEntity(assigneeId, title, message, 
                NotificationEntity.NotificationType.DEADLINE_APPROACHING);
        notification.setRelatedEntityType("TASK");
        notification.setRelatedEntityId(taskId);
        notification.setActionUrl("/tasks/" + taskId);
        notification.setPriority(daysUntilDeadline <= 1 ? 
                NotificationEntity.NotificationPriority.URGENT : 
                NotificationEntity.NotificationPriority.HIGH);
        
        createNotification(notification);
    }
    
    @Override
    public void notifyProjectUpdate(Long projectId, String updateMessage, List<Long> memberIds) {
        logger.info("Notifying project update: project={}, members={}", projectId, memberIds.size());
        
        String title = "Project Update";
        String message = updateMessage;
        
        for (Long memberId : memberIds) {
            NotificationEntity notification = new NotificationEntity(memberId, title, message, 
                    NotificationEntity.NotificationType.PROJECT_UPDATED);
            notification.setRelatedEntityType("PROJECT");
            notification.setRelatedEntityId(projectId);
            notification.setActionUrl("/projects/" + projectId);
            
            createNotification(notification);
        }
    }
    
    @Override
    public void notifyMention(Long mentionedUserId, Long mentionerUserId, String context, String entityType, Long entityId) {
        logger.info("Notifying mention: mentioned={}, mentioner={}, entity={}:{}", 
                mentionedUserId, mentionerUserId, entityType, entityId);
        
        String title = "You were mentioned";
        String message = "You were mentioned in " + context;
        
        NotificationEntity notification = new NotificationEntity(mentionedUserId, title, message, 
                NotificationEntity.NotificationType.MENTION);
        notification.setSenderId(mentionerUserId);
        notification.setRelatedEntityType(entityType);
        notification.setRelatedEntityId(entityId);
        notification.setPriority(NotificationEntity.NotificationPriority.HIGH);
        
        createNotification(notification);
    }
    
    @Override
    public void notifyFileUpload(Long uploaderId, String fileName, Long projectId, List<Long> memberIds) {
        logger.info("Notifying file upload: uploader={}, file={}, project={}", uploaderId, fileName, projectId);
        
        String title = "New File Uploaded";
        String message = String.format("New file '%s' has been uploaded to the project.", fileName);
        
        for (Long memberId : memberIds) {
            if (!memberId.equals(uploaderId)) { // Don't notify the uploader
                NotificationEntity notification = new NotificationEntity(memberId, title, message, 
                        NotificationEntity.NotificationType.FILE_UPLOADED);
                notification.setSenderId(uploaderId);
                notification.setRelatedEntityType("PROJECT");
                notification.setRelatedEntityId(projectId);
                notification.setActionUrl("/projects/" + projectId + "/files");
                
                createNotification(notification);
            }
        }
    }
    
    // Helper method to get project member IDs
    // In a real implementation, this would query the project team members
    private List<Long> getProjectMemberIds(Long projectId) {
        // Placeholder implementation
        // In real scenario, query project team members from database
        return List.of(1L, 2L, 3L); // Mock data
    }
}