package com.aipm.ai_project_management.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    
    private Map<String, String> errors;
    
    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }
    
    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
    
    public ValidationException(String field, String message) {
        super(message);
        this.errors = new HashMap<>();
        this.errors.put(field, message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = new HashMap<>();
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
    
    public void addError(String field, String message) {
        if (this.errors == null) {
            this.errors = new HashMap<>();
        }
        this.errors.put(field, message);
    }
}

