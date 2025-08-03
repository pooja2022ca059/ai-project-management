package com.aipm.ai_project_management.modules.users.repository;

import com.aipm.ai_project_management.modules.users.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    
    @Query("SELECT up FROM UserPreference up WHERE up.user.id = :userId")
    Optional<UserPreference> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM UserPreference up WHERE up.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);

    boolean existsByUserId(Long userId);
}