package com.aipm.ai_project_management.modules.team.repository;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.team.entity.UserSkill;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser(User user);

    List<UserSkill> findByUserId(Long userId);

    List<UserSkill> findByNameIgnoreCase(String name);

    List<UserSkill> findByUserIdAndNameIgnoreCase(Long userId, String name);
}