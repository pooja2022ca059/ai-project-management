package com.aipm.ai_project_management.modules.auth.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.auth.dto.ForgotPasswordRequest;
import com.aipm.ai_project_management.modules.auth.dto.ResetPasswordRequest;
import com.aipm.ai_project_management.modules.auth.service.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Password Management", description = "Password reset and management APIs")
public class PasswordController {
    
    private final PasswordService passwordService;
    
    // Manual constructor instead of using @RequiredArgsConstructor
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
    
    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset", description = "Send password reset email to user")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest,
            HttpServletRequest request) {
        
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        
        passwordService.forgotPassword(forgotPasswordRequest, ipAddress, userAgent);
        
        // Using ApiResponse without the builder pattern
        ApiResponse<String> response = new ApiResponse<>(true, "Password reset email sent successfully", null);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password using token from email")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        
        passwordService.resetPassword(resetPasswordRequest);
        
        // Using ApiResponse without the builder pattern
        ApiResponse<String> response = new ApiResponse<>(true, "Password reset successfully", null);
        return ResponseEntity.ok(response);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}