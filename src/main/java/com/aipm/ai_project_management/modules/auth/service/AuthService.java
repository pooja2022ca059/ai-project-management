// AuthService.java
package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.modules.auth.dto.*;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, String ipAddress, String userAgent);
    void register(RegisterRequest registerRequest);
    void logout(String refreshToken);
    void logoutAllDevices(Long userId);
}

