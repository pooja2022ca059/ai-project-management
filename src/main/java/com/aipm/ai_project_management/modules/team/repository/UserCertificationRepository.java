package com.aipm.ai_project_management.modules.team.repository;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.team.entity.UserCertification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCertificationRepository extends JpaRepository<UserCertification, Long> {
    List<UserCertification> findByUser(User user);

    List<UserCertification> findByUserId(Long userId);

    List<UserCertification> findByNameIgnoreCase(String name);
}