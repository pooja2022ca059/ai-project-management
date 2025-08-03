package com.aipm.ai_project_management.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class AIServiceException extends RuntimeException {
    
    private String service;
    private String operation;
    
    public AIServiceException(String message) {
        super(message);
    }
    
    public AIServiceException(String message, String service, String operation) {
        super(message);
        this.service = service;
        this.operation = operation;
    }
    
    public AIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getService() {
        return service;
    }
    
    public String getOperation() {
        return operation;
    }
}

