package com.aipm.ai_project_management.modules.auth.service;

import com.aipm.ai_project_management.modules.auth.dto.PasswordStrengthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PasswordServiceTest {

    @Autowired
    private PasswordService passwordService;

    @Test
    void testPasswordStrengthEvaluation() {
        // Test weak password
        PasswordStrengthResponse weakResponse = passwordService.evaluatePasswordStrength("123");
        assertTrue(weakResponse.getScore() <= 2); // Should be weak or very weak
        assertFalse(weakResponse.isMeetsRequirements());
        assertFalse(weakResponse.getSuggestions().isEmpty());

        // Test strong password
        PasswordStrengthResponse strongResponse = passwordService.evaluatePasswordStrength("MyStr0ng!Password123");
        assertTrue(strongResponse.getScore() >= 4);
        assertTrue(strongResponse.isMeetsRequirements());

        // Test empty password
        PasswordStrengthResponse emptyResponse = passwordService.evaluatePasswordStrength("");
        assertEquals("Very Weak", emptyResponse.getStrength());
        assertEquals(0, emptyResponse.getScore());
        assertFalse(emptyResponse.isMeetsRequirements());
    }
}