package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.modules.auth.dto.RefreshTokenRequest;
import com.aipm.ai_project_management.modules.auth.dto.RefreshTokenResponse;

public interface JwtService {
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    void revokeToken(String token);
    boolean validateToken(String token);
}