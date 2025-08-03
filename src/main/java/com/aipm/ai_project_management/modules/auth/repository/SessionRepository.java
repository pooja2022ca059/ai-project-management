package com.aipm.ai_project_management.modules.auth.repository;

import com.aipm.ai_project_management.modules.auth.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long> {
    
    Optional<UserSession> findByRefreshToken(String refreshToken);
    
    List<UserSession> findByUserIdAndActiveTrue(Long userId);
    
    @Query("SELECT s FROM UserSession s WHERE s.user.id = :userId AND s.active = true AND s.expiresAt > :now")
    List<UserSession> findActiveSessionsByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    @Modifying
    @Query("UPDATE UserSession s SET s.active = false, s.revokedAt = :now, s.revokedReason = :reason " +
           "WHERE s.user.id = :userId AND s.active = true")
    void revokeAllUserSessions(@Param("userId") Long userId, 
                              @Param("now") LocalDateTime now, 
                              @Param("reason") String reason);
    
    @Modifying
    @Query("UPDATE UserSession s SET s.active = false, s.revokedAt = :now, s.revokedReason = 'Expired' " +
           "WHERE s.expiresAt < :now AND s.active = true")
    void revokeExpiredSessions(@Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(s) FROM UserSession s WHERE s.user.id = :userId AND s.active = true AND s.expiresAt > :now")
    long countActiveSessionsByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.revokedAt < :cutoffDate OR (s.expiresAt < :cutoffDate AND s.active = false)")
    void deleteOldSessions(@Param("cutoffDate") LocalDateTime cutoffDate);
}
