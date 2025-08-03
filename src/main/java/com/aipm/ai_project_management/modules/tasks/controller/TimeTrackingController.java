package com.aipm.ai_project_management.modules.tasks.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.tasks.dto.TimeLogRequest;
import com.aipm.ai_project_management.modules.tasks.dto.TimeTrackingDTO;
import com.aipm.ai_project_management.modules.tasks.service.TimeTrackingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TimeTrackingController {

    private final TimeTrackingService timeTrackingService;
    
    @Autowired
    public TimeTrackingController(TimeTrackingService timeTrackingService) {
        this.timeTrackingService = timeTrackingService;
    }

    @PostMapping("/tasks/{taskId}/time-log")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TimeTrackingDTO>> logTime(
            @PathVariable Long taskId,
            @Valid @RequestBody TimeLogRequest request) {
        
        request.setTaskId(taskId);
        TimeTrackingDTO timeLog = timeTrackingService.logTime(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(timeLog));
    }

    @GetMapping("/tasks/{taskId}/time-logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<List<TimeTrackingDTO>>> getTimeLogsByTaskId(
            @PathVariable Long taskId) {
        
        List<TimeTrackingDTO> timeLogs = timeTrackingService.getTimeLogsByTaskId(taskId);
        return ResponseEntity.ok(ApiResponse.success(timeLogs));
    }

    @GetMapping("/time-tracking/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER') or #userId == authentication.principal.id")
    public ResponseEntity<ApiResponse<List<TimeTrackingDTO>>> getUserTimeLogs(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<TimeTrackingDTO> timeLogs = timeTrackingService.getUserTimeLogs(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(timeLogs));
    }

    @PutMapping("/time-logs/{timeLogId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<TimeTrackingDTO>> updateTimeLog(
            @PathVariable Long timeLogId,
            @Valid @RequestBody TimeLogRequest request) {
        
        TimeTrackingDTO updatedTimeLog = timeTrackingService.updateTimeLog(timeLogId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedTimeLog));
    }

    @DeleteMapping("/time-logs/{timeLogId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<String>> deleteTimeLog(@PathVariable Long timeLogId) {
        timeTrackingService.deleteTimeLog(timeLogId);
        return ResponseEntity.ok(ApiResponse.success("Time log deleted successfully"));
    }

    @GetMapping("/tasks/{taskId}/time-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTaskTimeSummary(
            @PathVariable Long taskId) {
        
        Map<String, Object> summary = timeTrackingService.getTimeTrackingSummary(taskId);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}