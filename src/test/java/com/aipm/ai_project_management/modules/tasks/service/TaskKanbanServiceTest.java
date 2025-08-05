package com.aipm.ai_project_management.modules.tasks.service;

import com.aipm.ai_project_management.modules.tasks.dto.MoveTaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskKanbanServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    void testMoveTaskToColumnWithInvalidTask() {
        // Test that invalid task ID throws exception
        MoveTaskRequest moveRequest = new MoveTaskRequest(99999L, "IN_PROGRESS", null);
        
        assertThrows(RuntimeException.class, () -> {
            taskService.moveTaskToColumn(1L, moveRequest);
        });
    }
    
    @Test
    void testMoveTaskToColumnWithInvalidStatus() {
        // Test that invalid status throws exception
        MoveTaskRequest moveRequest = new MoveTaskRequest(1L, "INVALID_STATUS", null);
        
        assertThrows(RuntimeException.class, () -> {
            taskService.moveTaskToColumn(1L, moveRequest);
        });
    }
}