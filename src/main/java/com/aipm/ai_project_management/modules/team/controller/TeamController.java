package com.aipm.ai_project_management.modules.team.controller;



import com.aipm.ai_project_management.modules.team.dto.*;
import com.aipm.ai_project_management.modules.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for team management.
 */
@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamMemberDTO>> listTeamMembers(
           @RequestParam(required = false) String search,
           @RequestParam(required = false) String role,
           @RequestParam(required = false) String status,
           @RequestParam(required = false) List<String> skills,
           @RequestParam(defaultValue = "1") int page,
           @RequestParam(defaultValue = "20") int limit) {
        List<TeamMemberDTO> members = teamService.listTeamMembers(search, role, status, skills, page, limit);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamMemberDTO> getTeamMemberProfile(@PathVariable Long id) {
        TeamMemberDTO member = teamService.getTeamMemberProfile(id);
        return ResponseEntity.ok(member);
    }

    @PostMapping
    public ResponseEntity<TeamMemberDTO> addTeamMember(@RequestBody TeamMemberDTO request) {
        TeamMemberDTO created = teamService.addTeamMember(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamMemberDTO> updateTeamMember(@PathVariable Long id, @RequestBody TeamMemberDTO request) {
        TeamMemberDTO updated = teamService.updateTeamMember(id, request);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/performance/{userId}")
    public ResponseEntity<List<PerformanceDTO>> getPerformance(@PathVariable Long userId) {
        return ResponseEntity.ok(teamService.getUserPerformance(userId));
    }

    @PostMapping("/performance/{userId}")
    public ResponseEntity<PerformanceDTO> addOrUpdatePerformance(@PathVariable Long userId, @RequestBody PerformanceDTO dto) {
        return ResponseEntity.ok(teamService.addOrUpdateUserPerformance(userId, dto));
    }

    // Add more endpoints for workload, performance as needed...
}
