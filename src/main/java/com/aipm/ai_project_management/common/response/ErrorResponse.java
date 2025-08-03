package com.aipm.ai_project_management.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private boolean success = false;
    private String error;
    private String message;
    private Integer status;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;
    private List<String> errors;
    
    // Constructors
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String error, String message, Integer status, String path) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
    
    // Builder pattern
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
    
    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    // Builder class
    public static class ErrorResponseBuilder {
        private String error;
        private String message;
        private Integer status;
        private String path;
        private Map<String, String> validationErrors;
        private List<String> errors;
        
        ErrorResponseBuilder() {
        }
        
        public ErrorResponseBuilder error(String error) {
            this.error = error;
            return this;
        }
        
        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public ErrorResponseBuilder status(Integer status) {
            this.status = status;
            return this;
        }
        
        public ErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }
        
        public ErrorResponseBuilder validationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }
        
        public ErrorResponseBuilder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }
        
        public ErrorResponse build() {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.error = this.error;
            errorResponse.message = this.message;
            errorResponse.status = this.status;
            errorResponse.path = this.path;
            errorResponse.validationErrors = this.validationErrors;
            errorResponse.errors = this.errors;
            return errorResponse;
        }
    }
}

