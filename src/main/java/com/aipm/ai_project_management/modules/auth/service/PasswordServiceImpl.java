package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.common.exceptions.UnauthorizedException;
import com.aipm.ai_project_management.common.exceptions.ValidationException;
import com.aipm.ai_project_management.modules.auth.dto.ForgotPasswordRequest;
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
import java.util.UUID;

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
}