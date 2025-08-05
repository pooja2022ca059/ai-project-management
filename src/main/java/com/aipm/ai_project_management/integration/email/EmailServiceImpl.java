package com.aipm.ai_project_management.integration.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.from:noreply@aipm.com}")
    private String fromEmail;

    @Value("${app.name:AI Project Management}")
    private String appName;

    @Value("${app.url:http://localhost:3000}")
    private String appUrl;

    @Override
    public void sendPasswordResetEmail(String email, String name, String token) {
        logger.info("Sending password reset email to: {}", email);
        
        String subject = "Password Reset Request - " + appName;
        String resetUrl = appUrl + "/reset-password?token=" + token;
        
        String htmlContent = generatePasswordResetTemplate(name, resetUrl);
        
        try {
            sendHtmlEmail(email, subject, htmlContent);
            logger.info("Password reset email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send password reset email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public void sendVerificationEmail(String email, String name, String token) {
        logger.info("Sending verification email to: {}", email);
        
        String subject = "Verify Your Account - " + appName;
        String verificationUrl = appUrl + "/verify-email?token=" + token;
        
        String htmlContent = generateVerificationTemplate(name, verificationUrl);
        
        try {
            sendHtmlEmail(email, subject, htmlContent);
            logger.info("Verification email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(String email, String name) {
        logger.info("Sending welcome email to: {}", email);
        
        String subject = "Welcome to " + appName + "!";
        String htmlContent = generateWelcomeTemplate(name);
        
        try {
            sendHtmlEmail(email, subject, htmlContent);
            logger.info("Welcome email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        logger.info("Sending email to: {} with subject: {}", to, subject);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            emailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        logger.info("Sending HTML email to: {} with subject: {}", to, subject);
        
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        emailSender.send(message);
        logger.info("HTML email sent successfully to: {}", to);
    }

    @Override
    public void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        logger.info("Sending templated email to: {} using template: {}", to, templateName);
        
        try {
            String htmlContent = processTemplate(templateName, variables);
            sendHtmlEmail(to, subject, htmlContent);
            logger.info("Templated email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send templated email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send templated email", e);
        }
    }

    @Override
    public void sendProjectNotificationEmail(String email, String projectName, String message, String actionUrl) {
        logger.info("Sending project notification email to: {} for project: {}", email, projectName);
        
        String subject = "Project Update: " + projectName;
        String htmlContent = generateProjectNotificationTemplate(projectName, message, actionUrl);
        
        try {
            sendHtmlEmail(email, subject, htmlContent);
            logger.info("Project notification email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send project notification email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send project notification email", e);
        }
    }

    @Override
    public void sendTaskAssignmentEmail(String email, String taskName, String projectName, String assignedBy, String dueDate) {
        logger.info("Sending task assignment email to: {} for task: {}", email, taskName);
        
        String subject = "Task Assigned: " + taskName;
        String htmlContent = generateTaskAssignmentTemplate(taskName, projectName, assignedBy, dueDate);
        
        try {
            sendHtmlEmail(email, subject, htmlContent);
            logger.info("Task assignment email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send task assignment email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send task assignment email", e);
        }
    }

    @Override
    public void sendDeadlineReminderEmail(String email, String taskName, String daysRemaining) {
        logger.info("Sending deadline reminder email to: {} for task: {}", email, taskName);
        
        String subject = "Deadline Reminder: " + taskName;
        String htmlContent = generateDeadlineReminderTemplate(taskName, daysRemaining);
        
        try {
            sendHtmlEmail(email, subject, htmlContent);
            logger.info("Deadline reminder email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send deadline reminder email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send deadline reminder email", e);
        }
    }

    // Template generation methods
    
    private String generatePasswordResetTemplate(String name, String resetUrl) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Password Reset</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4f46e5; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 24px; background-color: #4f46e5; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }
                    .footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>%s</h1>
                    </div>
                    <div class="content">
                        <h2>Hello %s,</h2>
                        <p>We received a request to reset your password. Click the button below to reset it:</p>
                        <a href="%s" class="button">Reset Password</a>
                        <p>If you didn't request this, please ignore this email. The link will expire in 24 hours.</p>
                        <p>If the button doesn't work, copy and paste this link into your browser:</p>
                        <p>%s</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, appName, name, resetUrl, resetUrl, appName);
    }
    
    private String generateVerificationTemplate(String name, String verificationUrl) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Email Verification</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #059669; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 24px; background-color: #059669; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }
                    .footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Verify Your Email</h1>
                    </div>
                    <div class="content">
                        <h2>Welcome %s!</h2>
                        <p>Thank you for signing up for %s. Please verify your email address by clicking the button below:</p>
                        <a href="%s" class="button">Verify Email</a>
                        <p>If the button doesn't work, copy and paste this link into your browser:</p>
                        <p>%s</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, name, appName, verificationUrl, verificationUrl, appName);
    }
    
    private String generateWelcomeTemplate(String name) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Welcome</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #7c3aed; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 24px; background-color: #7c3aed; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }
                    .footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to %s!</h1>
                    </div>
                    <div class="content">
                        <h2>Hello %s,</h2>
                        <p>Welcome to %s! We're excited to have you on board.</p>
                        <p>You can now start managing your projects, tasks, and teams with our AI-powered platform.</p>
                        <a href="%s" class="button">Get Started</a>
                        <p>If you have any questions, feel free to reach out to our support team.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, appName, name, appName, appUrl, appName);
    }
    
    private String generateProjectNotificationTemplate(String projectName, String message, String actionUrl) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Project Update</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #dc2626; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 24px; background-color: #dc2626; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }
                    .footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Project Update</h1>
                    </div>
                    <div class="content">
                        <h2>Project: %s</h2>
                        <p>%s</p>
                        %s
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, projectName, message, 
            actionUrl != null ? String.format("<a href=\"%s\" class=\"button\">View Project</a>", actionUrl) : "",
            appName);
    }
    
    private String generateTaskAssignmentTemplate(String taskName, String projectName, String assignedBy, String dueDate) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Task Assignment</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #ea580c; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 24px; background-color: #ea580c; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }
                    .task-details { background-color: white; padding: 15px; border-radius: 4px; margin: 15px 0; }
                    .footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>New Task Assignment</h1>
                    </div>
                    <div class="content">
                        <h2>You've been assigned a new task!</h2>
                        <div class="task-details">
                            <h3>%s</h3>
                            <p><strong>Project:</strong> %s</p>
                            <p><strong>Assigned by:</strong> %s</p>
                            <p><strong>Due Date:</strong> %s</p>
                        </div>
                        <a href="%s" class="button">View Task</a>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, taskName, projectName, assignedBy, dueDate, appUrl, appName);
    }
    
    private String generateDeadlineReminderTemplate(String taskName, String daysRemaining) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Deadline Reminder</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f59e0b; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 24px; background-color: #f59e0b; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }
                    .reminder-box { background-color: #fef3c7; border-left: 4px solid #f59e0b; padding: 15px; margin: 15px 0; }
                    .footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>⏰ Deadline Reminder</h1>
                    </div>
                    <div class="content">
                        <h2>Task Deadline Approaching</h2>
                        <div class="reminder-box">
                            <h3>%s</h3>
                            <p><strong>Time Remaining:</strong> %s days</p>
                        </div>
                        <p>Don't forget to complete your task before the deadline!</p>
                        <a href="%s" class="button">View Task</a>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, taskName, daysRemaining, appUrl, appName);
    }
    
    private String processTemplate(String templateName, Map<String, Object> variables) {
        // Simple template processing - in production, consider using a template engine like Thymeleaf
        String template = getTemplate(templateName);
        
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            template = template.replace(placeholder, value);
        }
        
        return template;
    }
    
    private String getTemplate(String templateName) {
        // In production, load templates from files or database
        switch (templateName) {
            case "password-reset":
                return generatePasswordResetTemplate("{{name}}", "{{resetUrl}}");
            case "verification":
                return generateVerificationTemplate("{{name}}", "{{verificationUrl}}");
            case "welcome":
                return generateWelcomeTemplate("{{name}}");
            default:
                throw new IllegalArgumentException("Unknown template: " + templateName);
        }
    }
}