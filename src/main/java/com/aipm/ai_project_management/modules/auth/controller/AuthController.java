package com.aipm.ai_project_management.modules.auth.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.auth.dto.*;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.auth.security.UserPrincipal;
import com.aipm.ai_project_management.modules.auth.service.AuthService;
import com.aipm.ai_project_management.modules.auth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        
        LoginResponse response = authService.login(loginRequest, ipAddress, userAgent);
        
        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .success(true)
                .data(response)
                .build());
    }
    
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<String>builder()
                        .success(true)
                        .message("User registered successfully. Please check your email for verification.")
                        .build());
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Get new access token using refresh token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshRequest) {
        
        RefreshTokenResponse response = jwtService.refreshToken(refreshRequest);
        
        return ResponseEntity.ok(ApiResponse.<RefreshTokenResponse>builder()
                .success(true)
                .data(response)
                .build());
    }
    
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and invalidate refresh token")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest.getRefreshToken());
        
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Logged out successfully")
                .build());
    }
    
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<UserDto>builder()
                    .success(false)
                    .message("Unauthorized")
                    .build());
        }

        UserDto dto = new UserDto();
        dto.setId(principal.getId());
        dto.setEmail(principal.getEmail());
        dto.setName(principal.getName());
        dto.setAvatar(principal.getAvatar());
        dto.setRole(principal.getRole());

        return ResponseEntity.ok(
            ApiResponse.<UserDto>builder()
                .success(true)
                .data(dto)
                .build()
        );
    }
    
    @PostMapping("/logout-all")
    @Operation(summary = "Logout from all devices", description = "Invalidate all user sessions")
    public ResponseEntity<ApiResponse<String>> logoutAllDevices(@RequestBody LogoutAllRequest request) {
        authService.logoutAllDevices(request.getUserId());
        
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Logged out from all devices successfully")
                .build());
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}