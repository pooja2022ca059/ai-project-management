package com.aipm.ai_project_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class FileUploadConfig {

    @Value("${file.upload.dir:${user.home}/aipm-uploads}")
    private String uploadDir;

    @Value("${file.upload.max-file-size:10MB}")
    private String maxFileSize;

    @Value("${file.upload.max-request-size:50MB}")
    private String maxRequestSize;

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }
}