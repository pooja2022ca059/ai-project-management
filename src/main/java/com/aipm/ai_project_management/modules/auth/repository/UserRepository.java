package com.aipm.ai_project_management.modules.auth.repository;

import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.modules.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
 // In UserRepository
    Optional<User> findById(Long id);
    
    Page<User> findByRole(UserRole role, Pageable pageable);
    
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name, String email, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.active = true AND u.role = :role")
    List<User> findActiveUsersByRole(@Param("role") UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countActiveUsers();
    
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);
    
    @Modifying
    @Query("UPDATE User u SET u.loginAttempts = :attempts, u.lockedUntil = :lockedUntil WHERE u.id = :userId")
    void updateLoginAttempts(@Param("userId") Long userId, 
                           @Param("attempts") int attempts, 
                           @Param("lockedUntil") LocalDateTime lockedUntil);
    
    @Query("SELECT u FROM User u WHERE u.lockedUntil IS NOT NULL AND u.lockedUntil < :now")
    List<User> findUsersToUnlock(@Param("now") LocalDateTime now);
    
    
    
}

