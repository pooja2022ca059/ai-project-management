package com.aipm.ai_project_management.modules.auth.validation;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    
    // Common weak passwords
    private static final List<String> COMMON_PASSWORDS = List.of(
            "password", "12345678", "123456789", "password123", "admin123",
            "qwerty", "abc12345", "password1", "123456", "welcome123"
    );
    
    // Password patterns
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[@#$%^&+=!].*");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile(".*\\s.*");
    private static final Pattern SEQUENTIAL_PATTERN = Pattern.compile("(012|123|234|345|456|567|678|789|890|abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)");
    
    public class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final int strength; // 0-5
        
        public ValidationResult(boolean valid, List<String> errors, int strength) {
            this.valid = valid;
            this.errors = errors;
            this.strength = strength;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public int getStrength() {
            return strength;
        }
        
        public String getStrengthLabel() {
            switch (strength) {
                case 0:
                case 1:
                    return "Very Weak";
                case 2:
                    return "Weak";
                case 3:
                    return "Fair";
                case 4:
                    return "Strong";
                case 5:
                    return "Very Strong";
                default:
                    return "Unknown";
            }
        }
    }
    
    public ValidationResult validate(String password) {
        List<String> errors = new ArrayList<>();
        int strength = 0;
        
        if (password == null || password.isEmpty()) {
            errors.add("Password is required");
            return new ValidationResult(false, errors, strength);
        }
        
        // Length check
        if (password.length() < MIN_LENGTH) {
            errors.add("Password must be at least " + MIN_LENGTH + " characters long");
        } else if (password.length() > MAX_LENGTH) {
            errors.add("Password must be no more than " + MAX_LENGTH + " characters long");
        } else {
            strength++;
            if (password.length() >= 12) {
                strength++;
            }
        }
        
        // Common password check
        if (COMMON_PASSWORDS.stream().anyMatch(common -> 
                password.toLowerCase().contains(common.toLowerCase()))) {
            errors.add("Password is too common");
        }
        
        // Character type checks
        boolean hasUppercase = UPPERCASE_PATTERN.matcher(password).matches();
        boolean hasLowercase = LOWERCASE_PATTERN.matcher(password).matches();
        boolean hasDigit = DIGIT_PATTERN.matcher(password).matches();
        boolean hasSpecial = SPECIAL_CHAR_PATTERN.matcher(password).matches();
        
        if (!hasUppercase) {
            errors.add("Password must contain at least one uppercase letter");
        } else {
            strength++;
        }
        
        if (!hasLowercase) {
            errors.add("Password must contain at least one lowercase letter");
        }
        
        if (!hasDigit) {
            errors.add("Password must contain at least one digit");
        } else {
            strength++;
        }
        
        if (!hasSpecial) {
            errors.add("Password must contain at least one special character (@#$%^&+=!)");
        } else {
            strength++;
        }
        
        // Whitespace check
        if (WHITESPACE_PATTERN.matcher(password).matches()) {
            errors.add("Password must not contain whitespace");
        }
        
        // Sequential characters check
        if (SEQUENTIAL_PATTERN.matcher(password.toLowerCase()).find()) {
            errors.add("Password contains sequential characters");
            strength = Math.max(0, strength - 1);
        }
        
        // Repetitive characters check
        if (hasRepetitiveCharacters(password)) {
            errors.add("Password contains too many repetitive characters");
            strength = Math.max(0, strength - 1);
        }
        
        boolean isValid = errors.isEmpty();
        return new ValidationResult(isValid, errors, Math.min(5, strength));
    }
    
    private boolean hasRepetitiveCharacters(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && 
                password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isPasswordCompromised(String password, String email, String name) {
        String lowerPassword = password.toLowerCase();
        
        // Check if password contains email parts
        if (email != null && !email.isEmpty()) {
            String emailLocal = email.split("@")[0].toLowerCase();
            if (lowerPassword.contains(emailLocal) || emailLocal.contains(lowerPassword)) {
                return true;
            }
        }
        
        // Check if password contains name parts
        if (name != null && !name.isEmpty()) {
            String[] nameParts = name.toLowerCase().split("\\s+");
            for (String part : nameParts) {
                if (part.length() > 2 && lowerPassword.contains(part)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}

