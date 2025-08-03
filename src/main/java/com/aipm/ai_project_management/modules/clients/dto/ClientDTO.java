package com.aipm.ai_project_management.modules.clients.dto;

import com.aipm.ai_project_management.modules.clients.entity.Client.ClientStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ClientDTO {
    
    private Long id;
    private String name;
    private String logoUrl;
    private String email;
    private String phone;
    private String website;
    private String industry;
    private String companySize;
    private ClientStatus status;
    
    // Relationships
    private List<ClientContactDTO> contacts;
    private List<ClientAddressDTO> addresses;
    private Map<String, String> customFields;
    
    // Audit fields
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private String createdBy;
    private String updatedBy;
    
    // Counts (for list views)
    private Integer contactsCount;
    private Integer addressesCount;
    private Integer customFieldsCount;
    
    // Constructors
    public ClientDTO() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getCompanySize() {
        return companySize;
    }
    
    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }
    
    public ClientStatus getStatus() {
        return status;
    }
    
    public void setStatus(ClientStatus status) {
        this.status = status;
    }
    
    public List<ClientContactDTO> getContacts() {
        return contacts;
    }
    
    public void setContacts(List<ClientContactDTO> contacts) {
        this.contacts = contacts;
    }
    
    public List<ClientAddressDTO> getAddresses() {
        return addresses;
    }
    
    public void setAddresses(List<ClientAddressDTO> addresses) {
        this.addresses = addresses;
    }
    
    public Map<String, String> getCustomFields() {
        return customFields;
    }
    
    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Integer getContactsCount() {
        return contactsCount;
    }
    
    public void setContactsCount(Integer contactsCount) {
        this.contactsCount = contactsCount;
    }
    
    public Integer getAddressesCount() {
        return addressesCount;
    }
    
    public void setAddressesCount(Integer addressesCount) {
        this.addressesCount = addressesCount;
    }
    
    public Integer getCustomFieldsCount() {
        return customFieldsCount;
    }
    
    public void setCustomFieldsCount(Integer customFieldsCount) {
        this.customFieldsCount = customFieldsCount;
    }
}