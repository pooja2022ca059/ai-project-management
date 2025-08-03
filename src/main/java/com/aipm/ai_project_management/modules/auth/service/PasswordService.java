package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.modules.auth.dto.ForgotPasswordRequest;
import com.aipm.ai_project_management.modules.auth.dto.ResetPasswordRequest;

public interface PasswordService {
    void forgotPassword(ForgotPasswordRequest request, String ipAddress, String userAgent);
    void resetPassword(ResetPasswordRequest request);
    void changePassword(Long userId, String currentPassword, String newPassword);
}
