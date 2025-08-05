package com.aipm.ai_project_management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * File upload configuration with security and size limits.
 * Handles file storage, validation, and serving static files.
 */
@Configuration
@ConfigurationProperties(prefix = "app.file-upload")
public class FileUploadConfig implements WebMvcConfigurer {

    private String uploadDir = "uploads/";
    private long maxFileSize = 10 * 1024 * 1024; // 10MB
    private long maxRequestSize = 50 * 1024 * 1024; // 50MB
    private String[] allowedExtensions = {
        "jpg", "jpeg", "png", "gif", "bmp", "svg",  // Images
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",  // Documents
        "txt", "md", "csv",  // Text files
        "zip", "rar", "7z", "tar", "gz"  // Archives
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files statically
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir)
                .setCachePeriod(3600) // Cache for 1 hour
                .resourceChain(true);
    }

    /**
     * Validates if the file extension is allowed.
     */
    public boolean isAllowedExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return false;
        }
        
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        for (String allowedExt : allowedExtensions) {
            if (allowedExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates file size.
     */
    public boolean isValidFileSize(long fileSize) {
        return fileSize > 0 && fileSize <= maxFileSize;
    }

    /**
     * Gets the upload directory path.
     */
    public String getUploadPath() {
        return uploadDir;
    }

    /**
     * Generates a safe filename to prevent directory traversal attacks.
     */
    public String sanitizeFilename(String filename) {
        if (filename == null) {
            return "unknown";
        }
        
        // Remove path separators and other dangerous characters
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_")
                      .replaceAll("_{2,}", "_"); // Replace multiple underscores with single
    }

    // Getters and Setters for configuration properties
    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public long getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String[] getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(String[] allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }
}