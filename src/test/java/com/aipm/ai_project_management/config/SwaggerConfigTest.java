package com.aipm.ai_project_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void shouldConfigureOpenAPI() {
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("AI Project Management API");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("AI-powered project management software REST API documentation");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("bearerAuth");
    }
}