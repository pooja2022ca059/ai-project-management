package com.aipm.ai_project_management.modules.users.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.auth.security.UserPrincipal;
import com.aipm.ai_project_management.modules.users.dto.UserDTO;
import com.aipm.ai_project_management.modules.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserProfileController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUserProfile(Authentication authentication) {
        logger.info("GET /api/users/profile - Fetching current user profile");
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDTO userProfile = userService.getUserProfile(userPrincipal.getId());
        
        logger.info("Successfully fetched profile for user ID: {}", userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(userProfile));
    }
    
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or #userId == authentication.principal.id")
    public ResponseEntity<ApiResponse<UserDTO>> getUserProfile(@PathVariable Long userId) {
        logger.info("GET /api/users/{} - Fetching user profile", userId);
        
        UserDTO userProfile = userService.getUserProfile(userId);
        
        logger.info("Successfully fetched profile for user ID: {}", userId);
        return ResponseEntity.ok(ApiResponse.success(userProfile));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        logger.info("GET /api/users - Fetching all users");
        
        List<UserDTO> users = userService.getAllUsers();
        
        logger.info("Successfully fetched {} users", users.size());
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
