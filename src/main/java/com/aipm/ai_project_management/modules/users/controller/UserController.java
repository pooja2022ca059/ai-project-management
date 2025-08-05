package com.aipm.ai_project_management.modules.users.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.users.dto.UserDTO;
import com.aipm.ai_project_management.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "User Management", description = "Admin APIs for user management")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @PutMapping("/{userId}/status")
    @Operation(summary = "Update user status", description = "Update the status of a specific user (Admin only)")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<ApiResponse<UserDTO>> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam User.UserStatus status) {
        logger.info("PUT /api/admin/users/{}/status - Updating user status to: {}", userId, status);
        
        User updatedUser = userService.updateUserStatus(userId, status);
        UserDTO userDTO = new UserDTO(
            updatedUser.getId(),
            updatedUser.getEmail(),
            updatedUser.getName(),
            updatedUser.getAvatar(),
            updatedUser.getRole(),
            updatedUser.getStatus(),
            updatedUser.getPhone(),
            updatedUser.getLocation(),
            updatedUser.getTimezone(),
            updatedUser.getLanguage(),
            updatedUser.getEmailVerified(),
            updatedUser.getLastLoginAt(),
            updatedUser.getCreatedAt(),
            updatedUser.isActive()
        );
        
        logger.info("Successfully updated status for user ID: {}", userId);
        return ResponseEntity.ok(ApiResponse.success(userDTO));
    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId) {
        logger.info("DELETE /api/admin/users/{} - Deleting user", userId);
        
        userService.deleteUser(userId);
        
        logger.info("Successfully deleted user ID: {}", userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
    
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalUsersCount() {
        logger.info("GET /api/admin/users/count - Fetching total users count");
        
        long count = userService.getTotalUsersCount();
        
        logger.info("Total users count: {}", count);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}