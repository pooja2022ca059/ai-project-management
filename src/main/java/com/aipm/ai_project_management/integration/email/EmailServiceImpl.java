package com.aipm.ai_project_management.integration.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.from:noreply@aipm.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String email, String name, String token) {
        logger.info("Sending password reset email to: {}", email);
        
        try {
            String subject = "Password Reset Request - AI Project Management";
            String content = buildPasswordResetEmailContent(name, token);
            
            sendHtmlEmail(email, subject, content);
            logger.info("Password reset email sent successfully to: {}", email);
            
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", email, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public void sendVerificationEmail(String email, String name, String token) {
        logger.info("Sending verification email to: {}", email);
        
        try {
            String subject = "Email Verification - AI Project Management";
            String content = buildVerificationEmailContent(name, token);
            
            sendHtmlEmail(email, subject, content);
            logger.info("Verification email sent successfully to: {}", email);
            
        } catch (Exception e) {
            logger.error("Failed to send verification email to: {}", email, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(String email, String name) {
        logger.info("Sending welcome email to: {}", email);
        
        try {
            String subject = "Welcome to AI Project Management!";
            String content = buildWelcomeEmailContent(name);
            
            sendHtmlEmail(email, subject, content);
            logger.info("Welcome email sent successfully to: {}", email);
            
        } catch (Exception e) {
            logger.error("Failed to send welcome email to: {}", email, e);
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        logger.info("Sending email to: {} with subject: {}", to, subject);
        
        try {
            if (isHtmlContent(content)) {
                sendHtmlEmail(to, subject, content);
            } else {
                sendTextEmail(to, subject, content);
            }
            logger.info("Email sent successfully to: {}", to);
            
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // Additional methods for project-specific emails
    public void sendProjectInvitationEmail(String email, String userName, String projectName, String inviterName) {
        logger.info("Sending project invitation email to: {}", email);
        
        try {
            String subject = "Project Invitation - " + projectName;
            String content = buildProjectInvitationEmailContent(userName, projectName, inviterName);
            
            sendHtmlEmail(email, subject, content);
            logger.info("Project invitation email sent successfully to: {}", email);
            
        } catch (Exception e) {
            logger.error("Failed to send project invitation email to: {}", email, e);
            throw new RuntimeException("Failed to send project invitation email", e);
        }
    }

    public void sendTaskAssignmentEmail(String email, String userName, String taskName, String projectName) {
        logger.info("Sending task assignment email to: {}", email);
        
        try {
            String subject = "New Task Assignment - " + taskName;
            String content = buildTaskAssignmentEmailContent(userName, taskName, projectName);
            
            sendHtmlEmail(email, subject, content);
            logger.info("Task assignment email sent successfully to: {}", email);
            
        } catch (Exception e) {
            logger.error("Failed to send task assignment email to: {}", email, e);
            throw new RuntimeException("Failed to send task assignment email", e);
        }
    }

    public void sendDeadlineReminderEmail(String email, String userName, String taskName, String dueDate) {
        logger.info("Sending deadline reminder email to: {}", email);
        
        try {
            String subject = "Deadline Reminder - " + taskName;
            String content = buildDeadlineReminderEmailContent(userName, taskName, dueDate);
            
            sendHtmlEmail(email, subject, content);
            logger.info("Deadline reminder email sent successfully to: {}", email);
            
        } catch (Exception e) {
            logger.error("Failed to send deadline reminder email to: {}", email, e);
            throw new RuntimeException("Failed to send deadline reminder email", e);
        }
    }

    // Private helper methods
    private void sendTextEmail(String to, String subject, String content) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        
        javaMailSender.send(message);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException, MailException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        javaMailSender.send(message);
    }

    private boolean isHtmlContent(String content) {
        return StringUtils.hasText(content) && content.trim().startsWith("<");
    }

    // Email template methods
    private String buildPasswordResetEmailContent(String name, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Password Reset</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h1 style="color: #333; text-align: center;">Password Reset Request</h1>
                    
                    <p style="color: #666; font-size: 16px;">Hello %s,</p>
                    
                    <p style="color: #666; font-size: 16px;">
                        We received a request to reset your password for your AI Project Management account.
                        Click the button below to reset your password:
                    </p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #007bff; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            Reset Password
                        </a>
                    </div>
                    
                    <p style="color: #666; font-size: 14px;">
                        If you didn't request this password reset, please ignore this email. Your password will remain unchanged.
                    </p>
                    
                    <p style="color: #666; font-size: 14px;">
                        This link will expire in 24 hours for security reasons.
                    </p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        AI Project Management System<br>
                        This is an automated message, please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(name, resetLink);
    }

    private String buildVerificationEmailContent(String name, String token) {
        String verificationLink = frontendUrl + "/verify-email?token=" + token;
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Email Verification</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h1 style="color: #333; text-align: center;">Verify Your Email</h1>
                    
                    <p style="color: #666; font-size: 16px;">Hello %s,</p>
                    
                    <p style="color: #666; font-size: 16px;">
                        Thank you for registering with AI Project Management!
                        Please verify your email address by clicking the button below:
                    </p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #28a745; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            Verify Email
                        </a>
                    </div>
                    
                    <p style="color: #666; font-size: 14px;">
                        If you didn't create this account, please ignore this email.
                    </p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        AI Project Management System<br>
                        This is an automated message, please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(name, verificationLink);
    }

    private String buildWelcomeEmailContent(String name) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Welcome</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h1 style="color: #333; text-align: center;">Welcome to AI Project Management!</h1>
                    
                    <p style="color: #666; font-size: 16px;">Hello %s,</p>
                    
                    <p style="color: #666; font-size: 16px;">
                        Welcome to AI Project Management! We're excited to have you on board.
                    </p>
                    
                    <p style="color: #666; font-size: 16px;">
                        Here are some things you can do to get started:
                    </p>
                    
                    <ul style="color: #666; font-size: 16px;">
                        <li>Create your first project</li>
                        <li>Invite team members</li>
                        <li>Set up automation rules</li>
                        <li>Explore AI-powered insights</li>
                    </ul>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #007bff; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            Get Started
                        </a>
                    </div>
                    
                    <p style="color: #666; font-size: 14px;">
                        If you have any questions, feel free to contact our support team.
                    </p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        AI Project Management System<br>
                        This is an automated message, please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(name, frontendUrl);
    }

    private String buildProjectInvitationEmailContent(String userName, String projectName, String inviterName) {
        String acceptLink = frontendUrl + "/project-invitation";
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Project Invitation</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h1 style="color: #333; text-align: center;">Project Invitation</h1>
                    
                    <p style="color: #666; font-size: 16px;">Hello %s,</p>
                    
                    <p style="color: #666; font-size: 16px;">
                        %s has invited you to collaborate on the project "%s" in AI Project Management.
                    </p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #007bff; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            Accept Invitation
                        </a>
                    </div>
                    
                    <p style="color: #666; font-size: 14px;">
                        Click the button above to accept the invitation and start collaborating!
                    </p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        AI Project Management System<br>
                        This is an automated message, please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(userName, inviterName, projectName, acceptLink);
    }

    private String buildTaskAssignmentEmailContent(String userName, String taskName, String projectName) {
        String taskLink = frontendUrl + "/tasks";
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Task Assignment</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h1 style="color: #333; text-align: center;">New Task Assignment</h1>
                    
                    <p style="color: #666; font-size: 16px;">Hello %s,</p>
                    
                    <p style="color: #666; font-size: 16px;">
                        You have been assigned a new task "%s" in project "%s".
                    </p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #28a745; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            View Task
                        </a>
                    </div>
                    
                    <p style="color: #666; font-size: 14px;">
                        Click the button above to view the task details and get started!
                    </p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        AI Project Management System<br>
                        This is an automated message, please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(userName, taskName, projectName, taskLink);
    }

    private String buildDeadlineReminderEmailContent(String userName, String taskName, String dueDate) {
        String taskLink = frontendUrl + "/tasks";
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Deadline Reminder</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h1 style="color: #333; text-align: center;">⏰ Deadline Reminder</h1>
                    
                    <p style="color: #666; font-size: 16px;">Hello %s,</p>
                    
                    <p style="color: #666; font-size: 16px;">
                        This is a friendly reminder that your task "%s" is due on %s.
                    </p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #ffc107; color: black; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            View Task
                        </a>
                    </div>
                    
                    <p style="color: #666; font-size: 14px;">
                        Please make sure to complete the task on time. If you need any assistance, don't hesitate to reach out to your team!
                    </p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        AI Project Management System<br>
                        This is an automated message, please do not reply.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(userName, taskName, dueDate, taskLink);
    }
}