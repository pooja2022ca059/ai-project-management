package com.aipm.ai_project_management.modules.users.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.auth.security.UserPrincipal;
import com.aipm.ai_project_management.modules.users.dto.UserSettingsDTO;
import com.aipm.ai_project_management.modules.users.dto.UpdateUserSettingsRequest;
import com.aipm.ai_project_management.modules.users.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/settings")
@PreAuthorize("hasRole('ADMIN')")
public class UserSettingsController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserSettingsController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> getUserSettings(Authentication authentication) {
        logger.info("GET /api/settings/user - Fetching user settings");
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserSettingsDTO settings = userService.getUserSettings(userPrincipal.getId());
        
        logger.info("Successfully fetched user settings for user ID: {}", userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(settings));
    }
    
    @PutMapping("/user")
    public ResponseEntity<ApiResponse<UserSettingsDTO>> updateUserSettings(
            @Valid @RequestBody UpdateUserSettingsRequest request,
            Authentication authentication) {
        logger.info("PUT /api/settings/user - Updating user settings");
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserSettingsDTO updatedSettings = userService.updateUserSettings(userPrincipal.getId(), request);
        
        logger.info("Successfully updated user settings for user ID: {}", userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(updatedSettings));
    }
}
