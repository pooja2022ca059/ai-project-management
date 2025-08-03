package com.aipm.ai_project_management.modules.team.repository;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.modules.team.entity.Team;
import com.aipm.ai_project_management.modules.team.entity.TeamMember;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeam(Team team);

    List<TeamMember> findByUser(User user);

    Optional<TeamMember> findByTeamAndUser(Team team, User user);

    List<TeamMember> findByStatus(TeamMember.Status status);
}