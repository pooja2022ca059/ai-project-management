package com.aipm.ai_project_management.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    
    private String errorCode;
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}