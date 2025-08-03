package com.aipm.ai_project_management.modules.auth.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AuthValidator {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    public boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        return STRONG_PASSWORD_PATTERN.matcher(password).matches();
    }
    
    public String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        if (!Pattern.compile("(?=.*[0-9])").matcher(password).find()) {
            return "Password must contain at least one digit";
        }
        
        if (!Pattern.compile("(?=.*[a-z])").matcher(password).find()) {
            return "Password must contain at least one lowercase letter";
        }
        
        if (!Pattern.compile("(?=.*[A-Z])").matcher(password).find()) {
            return "Password must contain at least one uppercase letter";
        }
        
        if (!Pattern.compile("(?=.*[@#$%^&+=])").matcher(password).find()) {
            return "Password must contain at least one special character (@#$%^&+=)";
        }
        
        return "Password is strong";
    }
    
    public boolean arePasswordsMatching(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
}
