package com.aipm.ai_project_management.integration.email;

import jakarta.mail.MessagingException;
import java.util.Map;

public interface EmailService {
    
    /**
     * Send password reset email
     */
    void sendPasswordResetEmail(String email, String name, String token);
    
    /**
     * Send email verification email
     */
    void sendVerificationEmail(String email, String name, String token);
    
    /**
     * Send welcome email
     */
    void sendWelcomeEmail(String email, String name);
    
    /**
     * Send basic text email
     */
    void sendEmail(String to, String subject, String content);
    
    /**
     * Send HTML email
     */
    void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException;
    
    /**
     * Send templated email with variables
     */
    void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> variables);
    
    /**
     * Send project notification email
     */
    void sendProjectNotificationEmail(String email, String projectName, String message, String actionUrl);
    
    /**
     * Send task assignment email
     */
    void sendTaskAssignmentEmail(String email, String taskName, String projectName, String assignedBy, String dueDate);
    
    /**
     * Send deadline reminder email
     */
    void sendDeadlineReminderEmail(String email, String taskName, String daysRemaining);
}
