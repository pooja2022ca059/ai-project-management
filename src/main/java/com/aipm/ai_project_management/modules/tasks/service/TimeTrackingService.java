package com.aipm.ai_project_management.modules.tasks.service;

import com.aipm.ai_project_management.modules.tasks.dto.TimeLogRequest;
import com.aipm.ai_project_management.modules.tasks.dto.TimeTrackingDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TimeTrackingService {
    TimeTrackingDTO logTime(TimeLogRequest request);
    List<TimeTrackingDTO> getTimeLogsByTaskId(Long taskId);
    List<TimeTrackingDTO> getUserTimeLogs(Long userId, LocalDate startDate, LocalDate endDate);
    TimeTrackingDTO updateTimeLog(Long timeLogId, TimeLogRequest request);
    void deleteTimeLog(Long timeLogId);
    Map<String, Object> getTimeTrackingSummary(Long taskId);
}