package com.aipm.ai_project_management.modules.users.service;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.users.dto.UserDTO;
import com.aipm.ai_project_management.modules.users.dto.UserSettingsDTO;
import com.aipm.ai_project_management.modules.users.dto.UpdateUserSettingsRequest;

import java.util.List;

public interface UserService {
    
    // User Settings Management
    UserSettingsDTO getUserSettings(Long userId);
    UserSettingsDTO updateUserSettings(Long userId, UpdateUserSettingsRequest request);
    
    // User Profile Management
    User getUserById(Long userId);
    User getCurrentUser(String email);
    List<UserDTO> getAllUsers();
    UserDTO getUserProfile(Long userId);
    
    // User Management (Admin functions)
    User createUser(User user);
    User updateUserStatus(Long userId, User.UserStatus status);
    void deleteUser(Long userId);
    
    // Utility methods
    boolean existsByEmail(String email);
    long getTotalUsersCount();
}
