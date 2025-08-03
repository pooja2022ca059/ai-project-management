package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.common.exceptions.UnauthorizedException;
import com.aipm.ai_project_management.modules.auth.dto.RefreshTokenRequest;
import com.aipm.ai_project_management.modules.auth.dto.RefreshTokenResponse;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.auth.entity.UserSession;
import com.aipm.ai_project_management.modules.auth.repository.SessionRepository;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.auth.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JwtServiceImpl implements JwtService {
    
    // Replace @Slf4j with standard SLF4J logger
    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);
    
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    // Constructor to replace @RequiredArgsConstructor
    public JwtServiceImpl(SessionRepository sessionRepository, 
                          UserRepository userRepository,
                          JwtTokenProvider jwtTokenProvider) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        UserSession session = sessionRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        
        if (!session.isValid()) {
            throw new UnauthorizedException("Refresh token is expired or revoked");
        }
        
        User user = session.getUser();
        if (!user.isEnabled()) {
            throw new UnauthorizedException("User account is disabled");
        }
        
        // Generate new access token
        String newAccessToken = jwtTokenProvider.generateToken(user);
        
        return RefreshTokenResponse.builder()
                .token(newAccessToken)
                .expiresAt(jwtTokenProvider.getTokenExpiration())
                .build();
    }
    
    @Override
    public void revokeToken(String token) {
        // In a real implementation, you might want to maintain a blacklist of revoked tokens
        // For now, we'll just log the revocation
        log.info("Token revoked: {}", token);
    }
    
    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}