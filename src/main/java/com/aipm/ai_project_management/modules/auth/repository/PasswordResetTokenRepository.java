package com.aipm.ai_project_management.modules.auth.repository;

import com.aipm.ai_project_management.modules.auth.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    Optional<PasswordResetToken> findByTokenAndUsedFalse(String token);
    
    @Query("SELECT p FROM PasswordResetToken p WHERE p.user.id = :userId AND p.used = false AND p.expiresAt > :now")
    Optional<PasswordResetToken> findValidTokenByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    @Modifying
    @Query("UPDATE PasswordResetToken p SET p.used = true WHERE p.user.id = :userId AND p.used = false")
    void invalidateUserTokens(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiresAt < :cutoffDate OR p.used = true")
    void deleteExpiredTokens(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    long countByUserIdAndUsedFalseAndExpiresAtAfter(Long userId, LocalDateTime now);
}
