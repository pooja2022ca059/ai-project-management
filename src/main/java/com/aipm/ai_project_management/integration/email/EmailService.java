package com.aipm.ai_project_management.integration.email;


public interface EmailService {
    void sendPasswordResetEmail(String email, String name, String token);
    void sendVerificationEmail(String email, String name, String token);
    void sendWelcomeEmail(String email, String name);
    void sendEmail(String to, String subject, String content);
}
