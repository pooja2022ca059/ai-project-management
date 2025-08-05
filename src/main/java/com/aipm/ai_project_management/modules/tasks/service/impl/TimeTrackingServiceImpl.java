package com.aipm.ai_project_management.modules.tasks.service.impl;

import com.aipm.ai_project_management.modules.tasks.dto.TimeLogRequest;
import com.aipm.ai_project_management.modules.tasks.dto.TimeTrackingDTO;
import com.aipm.ai_project_management.modules.tasks.entity.Task;
import com.aipm.ai_project_management.modules.tasks.entity.TimeTracking;
import com.aipm.ai_project_management.modules.tasks.repository.TaskRepository;
import com.aipm.ai_project_management.modules.tasks.repository.TimeTrackingRepository;
import com.aipm.ai_project_management.modules.tasks.service.TimeTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimeTrackingServiceImpl implements TimeTrackingService {
    
    @Autowired
    private TimeTrackingRepository timeTrackingRepository;
    
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TimeTrackingDTO logTime(TimeLogRequest request) {
        // Validate task exists
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + request.getTaskId() + " not found"));
        
        // Create time tracking entity
        TimeTracking timeLog = new TimeTracking();
        timeLog.setTask(task);
        timeLog.setUserId(request.getUserId());
        timeLog.setHours(request.getHours());
        timeLog.setDescription(request.getDescription());
        timeLog.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        timeLog.setBillable(request.getBillable() != null ? request.getBillable() : true);
        timeLog.setCreatedAt(LocalDateTime.now());
        timeLog.setUpdatedAt(LocalDateTime.now());
        
        // Save to database
        TimeTracking savedTimeLog = timeTrackingRepository.save(timeLog);
        
        // Update task logged hours
        updateTaskLoggedHours(task);
        
        return convertToTimeTrackingDTO(savedTimeLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeTrackingDTO> getTimeLogsByTaskId(Long taskId) {
        List<TimeTracking> timeLogs = timeTrackingRepository.findByTaskIdOrderByDateDescCreatedAtDesc(taskId);
        return timeLogs.stream()
                .map(this::convertToTimeTrackingDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeTrackingDTO> getUserTimeLogs(Long userId, LocalDate startDate, LocalDate endDate) {
        List<TimeTracking> timeLogs = timeTrackingRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        return timeLogs.stream()
                .map(this::convertToTimeTrackingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TimeTrackingDTO updateTimeLog(Long timeLogId, TimeLogRequest request) {
        TimeTracking timeLog = timeTrackingRepository.findById(timeLogId)
                .orElseThrow(() -> new IllegalArgumentException("Time log with ID " + timeLogId + " not found"));
        
        // Update fields
        if (request.getHours() != null) {
            timeLog.setHours(request.getHours());
        }
        if (request.getDescription() != null) {
            timeLog.setDescription(request.getDescription());
        }
        if (request.getDate() != null) {
            timeLog.setDate(request.getDate());
        }
        if (request.getBillable() != null) {
            timeLog.setBillable(request.getBillable());
        }
        
        timeLog.setUpdatedAt(LocalDateTime.now());
        
        TimeTracking updatedTimeLog = timeTrackingRepository.save(timeLog);
        
        // Update task logged hours
        updateTaskLoggedHours(timeLog.getTask());
        
        return convertToTimeTrackingDTO(updatedTimeLog);
    }

    @Override
    public void deleteTimeLog(Long timeLogId) {
        TimeTracking timeLog = timeTrackingRepository.findById(timeLogId)
                .orElseThrow(() -> new IllegalArgumentException("Time log with ID " + timeLogId + " not found"));
        
        Task task = timeLog.getTask();
        timeTrackingRepository.delete(timeLog);
        
        // Update task logged hours after deletion
        updateTaskLoggedHours(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTimeTrackingSummary(Long taskId) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("taskId", taskId);
        
        Double totalHours = timeTrackingRepository.getTotalHoursByTaskId(taskId);
        summary.put("totalHours", totalHours != null ? totalHours : 0.0);
        
        Double totalBillableHours = timeTrackingRepository.getTotalBillableHoursByTaskId(taskId);
        summary.put("totalBillableHours", totalBillableHours != null ? totalBillableHours : 0.0);
        
        List<TimeTracking> timeLogs = timeTrackingRepository.findByTaskIdOrderByDateDescCreatedAtDesc(taskId);
        summary.put("entryCount", timeLogs.size());
        
        return summary;
    }
    
    // Helper methods
    private void updateTaskLoggedHours(Task task) {
        Double totalHours = timeTrackingRepository.getTotalHoursByTaskId(task.getId());
        task.setLoggedHours(totalHours != null ? totalHours : 0.0);
        taskRepository.save(task);
    }
    
    private TimeTrackingDTO convertToTimeTrackingDTO(TimeTracking timeLog) {
        TimeTrackingDTO dto = new TimeTrackingDTO();
        dto.setId(timeLog.getId());
        dto.setTaskId(timeLog.getTask().getId());
        dto.setTaskTitle(timeLog.getTask().getTitle());
        dto.setHours(timeLog.getHours());
        dto.setDescription(timeLog.getDescription());
        dto.setDate(timeLog.getDate());
        return dto;
    }
}