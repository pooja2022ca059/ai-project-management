package com.aipm.ai_project_management.modules.tasks.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.tasks.dto.TimeLogRequest;
import com.aipm.ai_project_management.modules.tasks.dto.TimeTrackingDTO;
import com.aipm.ai_project_management.modules.tasks.entity.Task;
import com.aipm.ai_project_management.modules.tasks.entity.TimeTracking;
import com.aipm.ai_project_management.modules.tasks.repository.TaskRepository;
import com.aipm.ai_project_management.modules.tasks.repository.TimeTrackingRepository;
import com.aipm.ai_project_management.modules.auth.repository.UserRepository;
import com.aipm.ai_project_management.modules.tasks.service.TimeTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public TimeTrackingDTO logTime(TimeLogRequest request) {
        // Validate task exists
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));
        
        // Validate user exists
        if (!userRepository.existsById(request.getUserId())) {
            throw new ResourceNotFoundException("User not found with id: " + request.getUserId());
        }
        
        // Create time tracking entry
        TimeTracking timeLog = new TimeTracking();
        timeLog.setTask(task);
        timeLog.setUserId(request.getUserId());
        timeLog.setHours(request.getHours());
        timeLog.setDescription(request.getDescription());
        timeLog.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        timeLog.setBillable(request.getBillable() != null ? request.getBillable() : true);
        timeLog.setHourlyRate(null); // Will be set from user profile or project settings
        timeLog.setCreatedAt(LocalDateTime.now());
        timeLog.setUpdatedAt(LocalDateTime.now());
        
        TimeTracking savedTimeLog = timeTrackingRepository.save(timeLog);
        
        // Update task's logged hours
        updateTaskLoggedHours(task);
        
        return convertToTimeTrackingDTO(savedTimeLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeTrackingDTO> getTimeLogsByTaskId(Long taskId) {
        // Validate task exists
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        
        List<TimeTracking> timeLogs = timeTrackingRepository.findByTaskIdOrderByDateDescCreatedAtDesc(taskId);
        
        return timeLogs.stream()
                .map(this::convertToTimeTrackingDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeTrackingDTO> getUserTimeLogs(Long userId, LocalDate startDate, LocalDate endDate) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        List<TimeTracking> timeLogs = timeTrackingRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        
        return timeLogs.stream()
                .map(this::convertToTimeTrackingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TimeTrackingDTO updateTimeLog(Long timeLogId, TimeLogRequest request) {
        TimeTracking timeLog = timeTrackingRepository.findById(timeLogId)
                .orElseThrow(() -> new ResourceNotFoundException("Time log not found with id: " + timeLogId));
        
        // Update fields if provided
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
        // Note: hourlyRate not available in TimeLogRequest, would need to be added if needed
        
        timeLog.setUpdatedAt(LocalDateTime.now());
        
        TimeTracking updatedTimeLog = timeTrackingRepository.save(timeLog);
        
        // Update task's logged hours
        updateTaskLoggedHours(timeLog.getTask());
        
        return convertToTimeTrackingDTO(updatedTimeLog);
    }

    @Override
    public void deleteTimeLog(Long timeLogId) {
        TimeTracking timeLog = timeTrackingRepository.findById(timeLogId)
                .orElseThrow(() -> new ResourceNotFoundException("Time log not found with id: " + timeLogId));
        
        Task task = timeLog.getTask();
        timeTrackingRepository.delete(timeLog);
        
        // Update task's logged hours
        updateTaskLoggedHours(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTimeTrackingSummary(Long taskId) {
        // Validate task exists
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        
        Map<String, Object> summary = new HashMap<>();
        
        Double totalHours = timeTrackingRepository.getTotalHoursByTaskId(taskId);
        Double billableHours = timeTrackingRepository.getTotalBillableHoursByTaskId(taskId);
        
        summary.put("taskId", taskId);
        summary.put("totalHours", totalHours != null ? totalHours : 0.0);
        summary.put("billableHours", billableHours != null ? billableHours : 0.0);
        summary.put("nonBillableHours", (totalHours != null ? totalHours : 0.0) - (billableHours != null ? billableHours : 0.0));
        
        return summary;
    }
    
    // Helper method to convert TimeTracking entity to TimeTrackingDTO
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
    
    // Helper method to update task's logged hours
    private void updateTaskLoggedHours(Task task) {
        Double totalHours = timeTrackingRepository.getTotalHoursByTaskId(task.getId());
        task.setLoggedHours(totalHours != null ? totalHours : 0.0);
        taskRepository.save(task);
    }
}