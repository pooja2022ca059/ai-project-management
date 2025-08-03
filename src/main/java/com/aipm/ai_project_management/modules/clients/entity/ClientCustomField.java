package com.aipm.ai_project_management.modules.clients.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_custom_fields")
public class ClientCustomField {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client client;
    
    @Column(name = "field_name", nullable = false)
    private String fieldName;
    
    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public ClientCustomField() {
        this.createdAt = LocalDateTime.now();
    }
    
    public ClientCustomField(String fieldName, String fieldValue) {
        this();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getFieldValue() {
        return fieldValue;
    }
    
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "ClientCustomField{" +
                "id=" + id +
                ", fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                '}';
    }
}
