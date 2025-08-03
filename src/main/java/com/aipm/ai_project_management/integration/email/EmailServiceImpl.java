package com.aipm.ai_project_management.integration.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.from:noreply@aipm.com}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(String email, String name, String token) {
        // In a real application, this would send an actual email
        // For now, just log it
        System.out.println("Sending password reset email to " + email);
        System.out.println("Token: " + token);
        System.out.println("Name: " + name);
    }

	@Override
	public void sendVerificationEmail(String email, String name, String token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendWelcomeEmail(String email, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEmail(String to, String subject, String content) {
		// TODO Auto-generated method stub
		
	}

    // Add other email methods as defined in your EmailService interface
}