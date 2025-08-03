package com.aipm.ai_project_management.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private Object error;
    private LocalDateTime timestamp;
    
    // Constructors
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiResponse(boolean success, String message, T data, Object error) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
    
    // Static factory methods
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    public static <T> ApiResponse<T> error(String message, Object error) {
        return new ApiResponse<>(false, message, null, error);
    }
    
    // Builder pattern
    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Object getError() {
        return error;
    }
    
    public void setError(Object error) {
        this.error = error;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    // Builder class
    public static class ApiResponseBuilder<T> {
        private boolean success;
        private String message;
        private T data;
        private Object error;
        
        ApiResponseBuilder() {
        }
        
        public ApiResponseBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }
        
        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }
        
        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }
        
        public ApiResponseBuilder<T> error(Object error) {
            this.error = error;
            return this;
        }
        
        public ApiResponse<T> build() {
            return new ApiResponse<>(success, message, data, error);
        }
    }
}
