package com.aipm.ai_project_management.modules.clients.dto;

import com.aipm.ai_project_management.modules.clients.entity.Client.ClientStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public class UpdateClientRequest {
    
    @Size(min = 2, max = 255, message = "Client name must be between 2 and 255 characters")
    private String name;
    
    private String logoUrl;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;
    
    @Size(max = 255, message = "Website URL must not exceed 255 characters")
    private String website;
    
    @Size(max = 100, message = "Industry must not exceed 100 characters")
    private String industry;
    
    @Size(max = 50, message = "Company size must not exceed 50 characters")
    private String companySize;
    
    private ClientStatus status;
    
    @Valid
    private List<ClientContactDTO> contacts;
    
    @Valid
    private List<ClientAddressDTO> addresses;
    
    private Map<String, String> customFields;
    
    // Constructors
    public UpdateClientRequest() {
    }
    
    // Getters and Setters
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
}
