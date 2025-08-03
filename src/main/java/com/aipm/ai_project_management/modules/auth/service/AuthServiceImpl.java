package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.common.exceptions.UnauthorizedException;
import com.aipm.ai_project_management.common.exceptions.ValidationException;
import com.aipm.ai_project_management.modules.auth.dto.*;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.auth.entity.UserSession;
import com.aipm.ai_project_management.modules.auth.repository.SessionRepository;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.auth.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    
    // Replace @Slf4j with standard SLF4J logger
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    // Fields that were previously marked as final with @RequiredArgsConstructor
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    // Constructor to replace @RequiredArgsConstructor
    public AuthServiceImpl(AuthenticationManager authenticationManager, 
                          UserRepository userRepository,
                          SessionRepository sessionRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public LoginResponse login(LoginRequest loginRequest, String ipAddress, String userAgent) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        
        if (!user.isAccountNonLocked()) {
            throw new UnauthorizedException("Account is locked due to too many failed login attempts");
        }
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            
            // Reset login attempts on successful login
            user.resetLoginAttempts();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            // Generate tokens
            String accessToken = jwtTokenProvider.generateToken(user);
            String refreshToken = UUID.randomUUID().toString();
            
            // Create session
            UserSession session = UserSession.builder()
                    .user(user)
                    .refreshToken(refreshToken)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .expiresAt(loginRequest.isRememberMe() ? 
                            LocalDateTime.now().plusDays(30) : 
                            LocalDateTime.now().plusDays(7))
                    .active(true)
                    .build();
            sessionRepository.save(session);
            
            // Using builder methods directly
            return LoginResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .user(LoginResponse.UserDTO.fromUser(user))
                    .expiresAt(jwtTokenProvider.getTokenExpiration())
                    .build();
            
        } catch (Exception e) {
            // Increment login attempts on failure
            user.incrementLoginAttempts();
            userRepository.save(user);
            throw new UnauthorizedException("Invalid email or password");
        }
    }
    
    @Override
    public void register(RegisterRequest registerRequest) {
        // Validate passwords match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        
        // Create new user
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.valueOf(registerRequest.getRole()))
                .active(true)
                .emailVerified(false)
                .build();
        
        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());
        
        // TODO: Send verification email
    }
    
    @Override
    public void logout(String refreshToken) {
        UserSession session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        
        session.revoke("User logout");
        sessionRepository.save(session);
        
        log.info("User {} logged out", session.getUser().getEmail());
    }
    
    @Override
    public void logoutAllDevices(Long userId) {
        sessionRepository.revokeAllUserSessions(userId, LocalDateTime.now(), "Logout from all devices");
        log.info("All sessions revoked for user {}", userId);
    }
}