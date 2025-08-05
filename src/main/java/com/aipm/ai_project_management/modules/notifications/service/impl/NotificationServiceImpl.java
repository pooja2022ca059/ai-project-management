package com.aipm.ai_project_management.modules.notifications.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.notifications.dto.NotificationDTO;
import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import com.aipm.ai_project_management.modules.notifications.repository.NotificationRepository;
import com.aipm.ai_project_management.modules.notifications.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        logger.info("Creating notification for recipient: {}", notificationDTO.getRecipientId());
        
        // Validate recipient exists
        if (!userRepository.existsById(notificationDTO.getRecipientId())) {
            throw new ResourceNotFoundException("Recipient user not found with id: " + notificationDTO.getRecipientId());
        }
        
        // Validate sender if provided
        if (notificationDTO.getSenderId() != null && !userRepository.existsById(notificationDTO.getSenderId())) {
            throw new ResourceNotFoundException("Sender user not found with id: " + notificationDTO.getSenderId());
        }
        
        NotificationEntity notification = convertToEntity(notificationDTO);
        notification.setIsRead(false);
        
        NotificationEntity savedNotification = notificationRepository.save(notification);
        logger.info("Created notification with ID: {}", savedNotification.getId());
        
        return convertToDTO(savedNotification);
    }
    
    @Override
    public NotificationDTO sendNotification(Long recipientId, String title, String message, 
                                          NotificationEntity.NotificationType type) {
        return sendNotification(recipientId, null, title, message, type, 
                              NotificationEntity.NotificationPriority.NORMAL, null, null, null);
    }
    
    @Override
    public NotificationDTO sendNotification(Long recipientId, Long senderId, String title, String message,
                                          NotificationEntity.NotificationType type,
                                          NotificationEntity.NotificationPriority priority,
                                          String relatedEntityType, Long relatedEntityId, String actionUrl) {
        logger.info("Sending notification to user: {} with type: {}", recipientId, type);
        
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setRecipientId(recipientId);
        notificationDTO.setSenderId(senderId);
        notificationDTO.setTitle(title);
        notificationDTO.setMessage(message);
        notificationDTO.setType(type);
        notificationDTO.setPriority(priority != null ? priority : NotificationEntity.NotificationPriority.NORMAL);
        notificationDTO.setRelatedEntityType(relatedEntityType);
        notificationDTO.setRelatedEntityId(relatedEntityId);
        notificationDTO.setActionUrl(actionUrl);
        
        return createNotification(notificationDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        logger.info("Fetching notifications for user: {}", userId);
        
        Page<NotificationEntity> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> getUnreadNotifications(Long userId, Pageable pageable) {
        logger.info("Fetching unread notifications for user: {}", userId);
        
        Page<NotificationEntity> notifications = notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> getNotificationsByType(Long userId, NotificationEntity.NotificationType type, Pageable pageable) {
        logger.info("Fetching notifications of type {} for user: {}", type, userId);
        
        // Get all notifications of the specified type for the user
        List<NotificationEntity> notifications = notificationRepository.findByTypeAndRecipientId(type, userId);
        
        // Convert to DTOs
        List<NotificationDTO> notificationDTOs = notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        // Create a page response manually (simplified for this implementation)
        return new org.springframework.data.domain.PageImpl<>(notificationDTOs, pageable, notificationDTOs.size());
    }
    
    @Override
    public NotificationDTO markAsRead(Long notificationId) {
        logger.info("Marking notification as read: {}", notificationId);
        
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
        
        return convertToDTO(notification);
    }
    
    @Override
    public void markAllAsRead(Long userId) {
        logger.info("Marking all notifications as read for user: {}", userId);
        
        notificationRepository.markAllAsReadForUser(userId, LocalDateTime.now());
    }
    
    @Override
    public void deleteNotification(Long notificationId) {
        logger.info("Deleting notification: {}", notificationId);
        
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        notificationRepository.delete(notification);
    }
    
    @Override
    public void deleteAllUserNotifications(Long userId) {
        logger.info("Deleting all notifications for user: {}", userId);
        
        List<NotificationEntity> userNotifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, Pageable.unpaged()).getContent();
        notificationRepository.deleteAll(userNotifications);
    }
    
    @Override
    @Transactional(readOnly = true)
    public NotificationDTO getNotificationById(Long notificationId) {
        logger.info("Fetching notification by ID: {}", notificationId);
        
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        return convertToDTO(notification);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        logger.info("Getting unread count for user: {}", userId);
        
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }
    
    @Override
    public List<NotificationDTO> sendBulkNotifications(List<Long> recipientIds, String title, String message,
                                                      NotificationEntity.NotificationType type) {
        logger.info("Sending bulk notifications to {} recipients", recipientIds.size());
        
        List<NotificationDTO> createdNotifications = new ArrayList<>();
        
        for (Long recipientId : recipientIds) {
            try {
                NotificationDTO notification = sendNotification(recipientId, title, message, type);
                createdNotifications.add(notification);
            } catch (Exception e) {
                logger.error("Failed to send notification to user {}: {}", recipientId, e.getMessage());
            }
        }
        
        logger.info("Successfully sent {} out of {} bulk notifications", createdNotifications.size(), recipientIds.size());
        return createdNotifications;
    }
    
    @Override
    public void sendSystemNotification(String title, String message) {
        logger.info("Sending system notification to all users");
        
        // Get all user IDs (simplified - in production, consider pagination)
        List<Long> allUserIds = userRepository.findAll().stream()
            .map(user -> user.getId())
            .collect(Collectors.toList());
        
        sendBulkNotifications(allUserIds, title, message, NotificationEntity.NotificationType.SYSTEM_ALERT);
    }
    
    @Override
    public void cleanupOldNotifications(int daysOld) {
        logger.info("Cleaning up notifications older than {} days", daysOld);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        notificationRepository.deleteOldNotifications(cutoffDate);
        
        logger.info("Completed cleanup of old notifications");
    }
    
    @Override
    @Transactional(readOnly = true)
    public NotificationStats getUserNotificationStats(Long userId) {
        logger.info("Fetching notification statistics for user: {}", userId);
        
        long totalNotifications = countTotalNotificationsByUser(userId);
        long unreadCount = notificationRepository.countByRecipientIdAndIsReadFalse(userId);
        long readCount = totalNotifications - unreadCount;
        
        return new NotificationStats(totalNotifications, unreadCount, readCount);
    }
    
    // Private helper method for counting total notifications
    private long countTotalNotificationsByUser(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, Pageable.unpaged()).getTotalElements();
    }
    
    private NotificationDTO convertToDTO(NotificationEntity entity) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setRecipientId(entity.getRecipientId());
        dto.setSenderId(entity.getSenderId());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setType(entity.getType());
        dto.setPriority(entity.getPriority());
        dto.setIsRead(entity.getIsRead());
        dto.setReadAt(entity.getReadAt());
        dto.setRelatedEntityType(entity.getRelatedEntityType());
        dto.setRelatedEntityId(entity.getRelatedEntityId());
        dto.setActionUrl(entity.getActionUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
    
    private NotificationEntity convertToEntity(NotificationDTO dto) {
        NotificationEntity entity = new NotificationEntity();
        entity.setRecipientId(dto.getRecipientId());
        entity.setSenderId(dto.getSenderId());
        entity.setTitle(dto.getTitle());
        entity.setMessage(dto.getMessage());
        entity.setType(dto.getType());
        entity.setPriority(dto.getPriority() != null ? dto.getPriority() : NotificationEntity.NotificationPriority.NORMAL);
        entity.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
        entity.setReadAt(dto.getReadAt());
        entity.setRelatedEntityType(dto.getRelatedEntityType());
        entity.setRelatedEntityId(dto.getRelatedEntityId());
        entity.setActionUrl(dto.getActionUrl());
        return entity;
    }
}