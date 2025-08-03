package com.aipm.ai_project_management.modules.team.repository;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.team.entity.UserPerformance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPerformanceRepository extends JpaRepository<UserPerformance, Long> {
    List<UserPerformance> findByUser(User user);

    List<UserPerformance> findByUserId(Long userId);
}