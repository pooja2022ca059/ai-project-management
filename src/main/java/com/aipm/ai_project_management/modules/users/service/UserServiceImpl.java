package com.aipm.ai_project_management.modules.users.service;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.users.dto.UserDTO;
import com.aipm.ai_project_management.modules.users.dto.UserSettingsDTO;
import com.aipm.ai_project_management.modules.users.dto.UpdateUserSettingsRequest;
import com.aipm.ai_project_management.modules.users.entity.UserPreference;
import com.aipm.ai_project_management.modules.users.repository.UserPreferenceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserPreferenceRepository userPreferenceRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    @Transactional(readOnly = true)
    public UserSettingsDTO getUserSettings(Long userId) {
        logger.info("Fetching user settings for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // ✅ FIX: Use in-memory default preferences instead of saving to database
        UserPreference preferences = userPreferenceRepository.findByUserId(userId)
            .orElse(createInMemoryDefaultPreferences(user)); // ✅ No database save in read-only transaction
        
        return mapToUserSettingsDTO(user, preferences);
    }
    
    @Override
    public UserSettingsDTO updateUserSettings(Long userId, UpdateUserSettingsRequest request) {
        logger.info("Updating user settings for user ID: {}", userId);

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Update user profile fields
        boolean userUpdated = updateUserProfileFields(user, request);
        if (userUpdated) {
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            logger.info("Updated user profile for user ID: {}", userId);
        }

        // Update or create preferences
        UserPreference preferences = userPreferenceRepository.findByUserId(userId)
            .orElseGet(() -> createDefaultPreferences(user)); // ✅ This is fine in writable transaction

        boolean preferencesUpdated = updateUserPreferences(preferences, request);
        if (preferencesUpdated) {
            preferences.setUpdatedAt(LocalDateTime.now());
            userPreferenceRepository.save(preferences);
            logger.info("Updated user preferences for user ID: {}", userId);
        }

        return mapToUserSettingsDTO(user, preferences);
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        logger.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(String email) {
        logger.info("Fetching current user by email: {}", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(this::mapToUserDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserProfile(Long userId) {
        logger.info("Fetching user profile for user ID: {}", userId);
        User user = getUserById(userId);
        return mapToUserDTO(user);
    }
    
    @Override
    public User createUser(User user) {
        logger.info("Creating new user with email: {}", user.getEmail());
        
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        
        // Create default preferences for new user
        createDefaultPreferences(savedUser);
        
        logger.info("Successfully created user with ID: {}", savedUser.getId());
        return savedUser;
    }
    
    @Override
    public User updateUserStatus(Long userId, User.UserStatus status) {
        logger.info("Updating user status for user ID: {} to status: {}", userId, status);
        
        User user = getUserById(userId);
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        logger.info("Successfully updated user status for user ID: {}", userId);
        
        return updatedUser;
    }
    
    @Override
    public void deleteUser(Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        
        User user = getUserById(userId);
        
        // Delete user preferences first due to foreign key constraint
        userPreferenceRepository.deleteByUserId(userId);
        
        // Delete user
        userRepository.delete(user);
        
        logger.info("Successfully deleted user with ID: {}", userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.count();
    }
    
    // Private helper methods
    
    // ✅ NEW METHOD: Create preferences in-memory without saving to database
    private UserPreference createInMemoryDefaultPreferences(User user) {
        logger.debug("Creating in-memory default preferences for user ID: {}", user.getId());
        UserPreference preferences = new UserPreference(user);
        // Note: Not saving to database - just returning in-memory object
        logger.info("Using default preferences for user ID: {} (not saved to database)", user.getId());
        return preferences;
    }
    
    // ✅ EXISTING METHOD: Create and save preferences to database (for writable transactions)
    private UserPreference createDefaultPreferences(User user) {
        logger.debug("Creating and saving default preferences for user ID: {}", user.getId());
        UserPreference preferences = new UserPreference(user);
        return userPreferenceRepository.save(preferences);
    }
    
    private boolean updateUserProfileFields(User user, UpdateUserSettingsRequest request) {
        boolean updated = false;
        
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            String trimmedName = request.getName().trim();
            if (!trimmedName.equals(user.getName())) {
                user.setName(trimmedName);
                updated = true;
            }
        }
        
        if (request.getPhone() != null) {
            String trimmedPhone = request.getPhone().trim();
            if (!trimmedPhone.equals(user.getPhone())) {
                user.setPhone(trimmedPhone.isEmpty() ? null : trimmedPhone);
                updated = true;
            }
        }
        
        if (request.getLocation() != null) {
            String trimmedLocation = request.getLocation().trim();
            if (!trimmedLocation.equals(user.getLocation())) {
                user.setLocation(trimmedLocation.isEmpty() ? null : trimmedLocation);
                updated = true;
            }
        }
        
        if (request.getTimezone() != null) {
            String trimmedTimezone = request.getTimezone().trim();
            if (!trimmedTimezone.equals(user.getTimezone())) {
                user.setTimezone(trimmedTimezone.isEmpty() ? null : trimmedTimezone);
                updated = true;
            }
        }
        
        if (request.getLanguage() != null) {
            String trimmedLanguage = request.getLanguage().trim();
            if (!trimmedLanguage.equals(user.getLanguage())) {
                user.setLanguage(trimmedLanguage.isEmpty() ? null : trimmedLanguage);
                updated = true;
            }
        }
        
        if (request.getAvatar() != null) {
            String trimmedAvatar = request.getAvatar().trim();
            if (!trimmedAvatar.equals(user.getAvatar())) {
                user.setAvatar(trimmedAvatar.isEmpty() ? null : trimmedAvatar);
                updated = true;
            }
        }
        
        return updated;
    }
    
    private boolean updateUserPreferences(UserPreference preferences, UpdateUserSettingsRequest request) {
        boolean updated = false;
        
        if (request.getTheme() != null && !request.getTheme().equals(preferences.getTheme())) {
            preferences.setTheme(request.getTheme());
            updated = true;
        }
        
        if (request.getDateFormat() != null && !request.getDateFormat().equals(preferences.getDateFormat())) {
            preferences.setDateFormat(request.getDateFormat());
            updated = true;
        }
        
        if (request.getTimeFormat() != null && !request.getTimeFormat().equals(preferences.getTimeFormat())) {
            preferences.setTimeFormat(request.getTimeFormat());
            updated = true;
        }
        
        if (request.getItemsPerPage() != null && !request.getItemsPerPage().equals(preferences.getItemsPerPage())) {
            preferences.setItemsPerPage(request.getItemsPerPage());
            updated = true;
        }
        
        if (request.getDigestFrequency() != null && !request.getDigestFrequency().equals(preferences.getDigestFrequency())) {
            preferences.setDigestFrequency(request.getDigestFrequency());
            updated = true;
        }
        
        if (request.getEmailNotifications() != null) {
            try {
                String newEmailNotifications = objectMapper.writeValueAsString(request.getEmailNotifications());
                if (!newEmailNotifications.equals(preferences.getEmailNotifications())) {
                    preferences.setEmailNotifications(newEmailNotifications);
                    updated = true;
                }
            } catch (Exception e) {
                logger.error("Error serializing email notifications for user preferences", e);
            }
        }
        
        if (request.getPushNotifications() != null) {
            try {
                String newPushNotifications = objectMapper.writeValueAsString(request.getPushNotifications());
                if (!newPushNotifications.equals(preferences.getPushNotifications())) {
                    preferences.setPushNotifications(newPushNotifications);
                    updated = true;
                }
            } catch (Exception e) {
                logger.error("Error serializing push notifications for user preferences", e);
            }
        }
        
        return updated;
    }
    
    private UserSettingsDTO mapToUserSettingsDTO(User user, UserPreference preferences) {
        UserSettingsDTO dto = new UserSettingsDTO();
        
        // Map user fields
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setPhone(user.getPhone());
        dto.setLocation(user.getLocation());
        dto.setTimezone(user.getTimezone());
        dto.setLanguage(user.getLanguage());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setLastLoginAt(user.getLastLoginAt());
        
        // Map preferences
        dto.setTheme(preferences.getTheme());
        dto.setDateFormat(preferences.getDateFormat());
        dto.setTimeFormat(preferences.getTimeFormat());
        dto.setItemsPerPage(preferences.getItemsPerPage());
        dto.setDigestFrequency(preferences.getDigestFrequency());
        
        // Parse JSON notifications
        try {
            if (preferences.getEmailNotifications() != null) {
                dto.setEmailNotifications(objectMapper.readValue(
                    preferences.getEmailNotifications(), 
                    new TypeReference<Map<String, Boolean>>() {}
                ));
            } else {
                dto.setEmailNotifications(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error("Error parsing email notifications", e);
            dto.setEmailNotifications(new HashMap<>());
        }
        
        try {
            if (preferences.getPushNotifications() != null) {
                dto.setPushNotifications(objectMapper.readValue(
                    preferences.getPushNotifications(), 
                    new TypeReference<Map<String, Boolean>>() {}
                ));
            } else {
                dto.setPushNotifications(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error("Error parsing push notifications", e);
            dto.setPushNotifications(new HashMap<>());
        }
        
        return dto;
    }
    
    private UserDTO mapToUserDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getAvatar(),
            user.getRole(),
            user.getStatus(),
            user.getPhone(),
            user.getLocation(),
            user.getTimezone(),
            user.getLanguage(),
            user.getEmailVerified(),
            user.getLastLoginAt(),
            user.getCreatedAt(),
            user.isActive()
        );
    }
}