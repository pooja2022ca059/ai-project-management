package com.aipm.ai_project_management.modules.notifications.repository;

import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity;
import com.aipm.ai_project_management.modules.notifications.entity.NotificationEntity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // Find notifications by recipient
    Page<NotificationEntity> findByRecipientIdOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

    // Find unread notifications by recipient
    Page<NotificationEntity> findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

    // Count unread notifications for a user
    long countByRecipientIdAndIsReadFalse(Long recipientId);

    // Find notifications by type
    List<NotificationEntity> findByTypeAndRecipientId(NotificationType type, Long recipientId);

    // Find notifications related to a specific entity
    List<NotificationEntity> findByRelatedEntityTypeAndRelatedEntityId(String entityType, Long entityId);

    // Mark notification as read
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true, n.readAt = :readAt WHERE n.id = :id")
    void markAsRead(@Param("id") Long id, @Param("readAt") LocalDateTime readAt);

    // Mark all notifications as read for a user
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true, n.readAt = :readAt WHERE n.recipientId = :recipientId AND n.isRead = false")
    void markAllAsReadForUser(@Param("recipientId") Long recipientId, @Param("readAt") LocalDateTime readAt);

    // Delete old notifications (older than specified date)
    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.createdAt < :cutoffDate")
    void deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Find notifications by priority
    List<NotificationEntity> findByRecipientIdAndPriorityOrderByCreatedAtDesc(Long recipientId, NotificationEntity.NotificationPriority priority);
    
    // Find unread notifications without pagination
    List<NotificationEntity> findByRecipientIdAndIsReadFalse(Long recipientId);
    
    // Delete notifications by recipient
    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.recipientId = :recipientId")
    long deleteByRecipientId(@Param("recipientId") Long recipientId);
    
    // Delete notifications older than specified date
    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.createdAt < :cutoffDate")
    long deleteByCreatedAtBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
}