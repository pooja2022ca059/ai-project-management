package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.common.exceptions.UnauthorizedException;
import com.aipm.ai_project_management.common.exceptions.ValidationException;
import com.aipm.ai_project_management.modules.auth.dto.ForgotPasswordRequest;
import com.aipm.ai_project_management.modules.auth.dto.PasswordStrengthResponse;
import com.aipm.ai_project_management.modules.auth.dto.ResetPasswordRequest;
import com.aipm.ai_project_management.modules.auth.entity.PasswordResetToken;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.auth.repository.PasswordResetTokenRepository;
import com.aipm.ai_project_management.modules.auth.repository.SessionRepository;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.integration.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
public class PasswordServiceImpl implements PasswordService {
    
    // Replace @Slf4j with standard SLF4J logger
    private static final Logger log = LoggerFactory.getLogger(PasswordServiceImpl.class);
    
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SessionRepository sessionRepository; // Added this missing dependency
    
    @Value("${app.password-reset.token-validity-hours}")
    private int tokenValidityHours = 24;
    
    // Constructor to replace @RequiredArgsConstructor
    public PasswordServiceImpl(UserRepository userRepository,
                              PasswordResetTokenRepository tokenRepository,
                              PasswordEncoder passwordEncoder,
                              EmailService emailService,
                              SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.sessionRepository = sessionRepository;
    }
    
    @Override
    public void forgotPassword(ForgotPasswordRequest request, String ipAddress, String userAgent) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        
        // Check if user already has active reset tokens
        long activeTokens = tokenRepository.countByUserIdAndUsedFalseAndExpiresAtAfter(
                user.getId(), LocalDateTime.now());
        
        if (activeTokens >= 3) {
            throw new ValidationException("Too many password reset requests. Please try again later.");
        }
        
        // Invalidate any existing tokens
        tokenRepository.invalidateUserTokens(user.getId());
        
        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(tokenValidityHours))
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
        
        tokenRepository.save(resetToken);
        
        // Send reset email
        emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), token);
        
        log.info("Password reset token generated for user: {}", user.getEmail());
    }
    
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }
        
        PasswordResetToken resetToken = tokenRepository.findByTokenAndUsedFalse(request.getToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired reset token"));
        
        if (!resetToken.isValid()) {
            throw new UnauthorizedException("Reset token has expired");
        }
        
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.resetLoginAttempts();
        userRepository.save(user);
        
        // Mark token as used
        resetToken.markAsUsed();
        tokenRepository.save(resetToken);
        
        // Revoke all user sessions for security
        sessionRepository.revokeAllUserSessions(user.getId(), LocalDateTime.now(), "Password reset");
        
        log.info("Password reset successful for user: {}", user.getEmail());
    }
    
    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed for user: {}", user.getEmail());
    }
    
    @Override
    public PasswordStrengthResponse evaluatePasswordStrength(String password) {
        List<String> suggestions = new ArrayList<>();
        int score = 0;
        String strength = "Very Weak";
        
        if (password == null || password.isEmpty()) {
            suggestions.add("Password cannot be empty");
            return PasswordStrengthResponse.builder()
                    .strength(strength)
                    .score(score)
                    .suggestions(suggestions)
                    .meetsRequirements(false)
                    .build();
        }
        
        // Length check
        if (password.length() >= 8) {
            score += 1;
        } else {
            suggestions.add("Password should be at least 8 characters long");
        }
        
        if (password.length() >= 12) {
            score += 1;
        }
        
        // Uppercase check
        if (Pattern.compile("[A-Z]").matcher(password).find()) {
            score += 1;
        } else {
            suggestions.add("Password should contain at least one uppercase letter");
        }
        
        // Lowercase check
        if (Pattern.compile("[a-z]").matcher(password).find()) {
            score += 1;
        } else {
            suggestions.add("Password should contain at least one lowercase letter");
        }
        
        // Number check
        if (Pattern.compile("[0-9]").matcher(password).find()) {
            score += 1;
        } else {
            suggestions.add("Password should contain at least one number");
        }
        
        // Special character check
        if (Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            score += 1;
        } else {
            suggestions.add("Password should contain at least one special character");
        }
        
        // Common password check
        if (isCommonPassword(password)) {
            score -= 2;
            suggestions.add("Avoid using common passwords");
        }
        
        // Repeated characters check
        if (hasRepeatedCharacters(password)) {
            score -= 1;
            suggestions.add("Avoid repeating characters");
        }
        
        // Determine strength based on score
        if (score >= 6) {
            strength = "Very Strong";
        } else if (score >= 5) {
            strength = "Strong";
        } else if (score >= 3) {
            strength = "Medium";
        } else if (score >= 1) {
            strength = "Weak";
        }
        
        boolean meetsRequirements = score >= 4 && password.length() >= 8;
        
        if (suggestions.isEmpty()) {
            suggestions.add("Your password is strong!");
        }
        
        return PasswordStrengthResponse.builder()
                .strength(strength)
                .score(Math.max(0, score))
                .suggestions(suggestions)
                .meetsRequirements(meetsRequirements)
                .build();
    }
    
    private boolean isCommonPassword(String password) {
        // Basic common password check
        String[] commonPasswords = {
            "password", "123456", "123456789", "12345678", "12345", "1234567",
            "password123", "admin", "qwerty", "abc123", "Password1", "welcome",
            "letmein", "monkey", "1234567890"
        };
        
        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.contains(common.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasRepeatedCharacters(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && 
                password.charAt(i + 1) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}