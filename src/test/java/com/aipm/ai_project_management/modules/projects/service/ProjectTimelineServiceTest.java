package com.aipm.ai_project_management.modules.projects.service;

import com.aipm.ai_project_management.modules.projects.dto.ProjectTimelineDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProjectTimelineServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    void testGetProjectTimelineWithInvalidId() {
        // Test that invalid project ID throws exception
        assertThrows(RuntimeException.class, () -> {
            projectService.getProjectTimeline(99999L);
        });
    }
}