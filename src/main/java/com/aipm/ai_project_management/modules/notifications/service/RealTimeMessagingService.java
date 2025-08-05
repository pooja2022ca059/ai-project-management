package com.aipm.ai_project_management.modules.notifications.service;

import com.aipm.ai_project_management.modules.notifications.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealTimeMessagingService {
    
    private static final Logger logger = LoggerFactory.getLogger(RealTimeMessagingService.class);
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Send real-time notification to a specific user
     */
    public void sendNotificationToUser(Long userId, NotificationDTO notification) {
        logger.info("Sending real-time notification to user: {}", userId);
        
        String destination = "/user/" + userId + "/queue/notifications";
        messagingTemplate.convertAndSend(destination, notification);
    }
    
    /**
     * Send real-time update to a project channel
     */
    public void sendProjectUpdate(Long projectId, Object updateData) {
        logger.info("Sending project update to project: {}", projectId);
        
        String destination = "/topic/project/" + projectId + "/updates";
        messagingTemplate.convertAndSend(destination, updateData);
    }
    
    /**
     * Send real-time task update
     */
    public void sendTaskUpdate(Long taskId, Object taskUpdate) {
        logger.info("Sending task update for task: {}", taskId);
        
        String destination = "/topic/task/" + taskId + "/updates";
        messagingTemplate.convertAndSend(destination, taskUpdate);
    }
    
    /**
     * Send team notification
     */
    public void sendTeamNotification(Long teamId, Object notification) {
        logger.info("Sending team notification to team: {}", teamId);
        
        String destination = "/topic/team/" + teamId + "/notifications";
        messagingTemplate.convertAndSend(destination, notification);
    }
    
    /**
     * Send system-wide announcement
     */
    public void sendSystemAnnouncement(Object announcement) {
        logger.info("Sending system-wide announcement");
        
        String destination = "/topic/system/announcements";
        messagingTemplate.convertAndSend(destination, announcement);
    }
    
    /**
     * Send user status update (online/offline)
     */
    public void sendUserStatusUpdate(Long userId, String status) {
        logger.info("Sending user status update for user: {} - status: {}", userId, status);
        
        String destination = "/topic/user/" + userId + "/status";
        UserStatusUpdate statusUpdate = new UserStatusUpdate(userId, status, System.currentTimeMillis());
        messagingTemplate.convertAndSend(destination, statusUpdate);
    }
    
    /**
     * Send typing indicator
     */
    public void sendTypingIndicator(Long chatId, Long userId, boolean isTyping) {
        logger.debug("Sending typing indicator for user: {} in chat: {}", userId, chatId);
        
        String destination = "/topic/chat/" + chatId + "/typing";
        TypingIndicator indicator = new TypingIndicator(userId, isTyping, System.currentTimeMillis());
        messagingTemplate.convertAndSend(destination, indicator);
    }
    
    // Inner classes for structured data
    public static class UserStatusUpdate {
        private Long userId;
        private String status;
        private Long timestamp;
        
        public UserStatusUpdate(Long userId, String status, Long timestamp) {
            this.userId = userId;
            this.status = status;
            this.timestamp = timestamp;
        }
        
        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Long getTimestamp() { return timestamp; }
        public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    }
    
    public static class TypingIndicator {
        private Long userId;
        private boolean isTyping;
        private Long timestamp;
        
        public TypingIndicator(Long userId, boolean isTyping, Long timestamp) {
            this.userId = userId;
            this.isTyping = isTyping;
            this.timestamp = timestamp;
        }
        
        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public boolean isTyping() { return isTyping; }
        public void setTyping(boolean typing) { isTyping = typing; }
        public Long getTimestamp() { return timestamp; }
        public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    }
}