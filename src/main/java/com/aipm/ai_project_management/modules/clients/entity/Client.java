package com.aipm.ai_project_management.modules.clients.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "logo_url")
    private String logoUrl;
    
    private String email;
    private String phone;
    private String website;
    
    @Column(length = 100)
    private String industry;
    
    @Column(name = "company_size")
    private String companySize;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status = ClientStatus.ACTIVE;
    
    // Relationships - Only those that exist in your database
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClientContact> contacts = new ArrayList<>();
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClientAddress> addresses = new ArrayList<>();
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClientCustomField> customFields = new ArrayList<>();
    
    // Enums
    public enum ClientStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
    
    // Constructors
    public Client() {
        this.status = ClientStatus.ACTIVE;
    }
    
    public Client(String name) {
        this();
        this.name = name;
    }
    
    public Client(String name, String email) {
        this();
        this.name = name;
        this.email = email;
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
    
    public List<ClientContact> getContacts() {
        return contacts;
    }
    
    public void setContacts(List<ClientContact> contacts) {
        this.contacts = contacts;
    }
    
    public List<ClientAddress> getAddresses() {
        return addresses;
    }
    
    public void setAddresses(List<ClientAddress> addresses) {
        this.addresses = addresses;
    }
    
    public List<ClientCustomField> getCustomFields() {
        return customFields;
    }
    
    public void setCustomFields(List<ClientCustomField> customFields) {
        this.customFields = customFields;
    }
    
    // Helper methods for contacts
    public void addContact(ClientContact contact) {
        contacts.add(contact);
        contact.setClient(this);
    }
    
    public void removeContact(ClientContact contact) {
        contacts.remove(contact);
        contact.setClient(null);
    }
    
    // Helper methods for addresses
    public void addAddress(ClientAddress address) {
        addresses.add(address);
        address.setClient(this);
    }
    
    public void removeAddress(ClientAddress address) {
        addresses.remove(address);
        address.setClient(null);
    }
    
    // Helper methods for custom fields
    public void addCustomField(ClientCustomField customField) {
        customFields.add(customField);
        customField.setClient(this);
    }
    
    public void removeCustomField(ClientCustomField customField) {
        customFields.remove(customField);
        customField.setClient(null);
    }
    
    // Get primary contact
    public ClientContact getPrimaryContact() {
        return contacts.stream()
            .filter(ClientContact::getIsPrimary)
            .findFirst()
            .orElse(null);
    }
    
    // Get primary address
    public ClientAddress getPrimaryAddress() {
        return addresses.stream()
            .filter(ClientAddress::getIsPrimary)
            .findFirst()
            .orElse(null);
    }
    
    // Get custom field by name
    public String getCustomFieldValue(String fieldName) {
        return customFields.stream()
            .filter(field -> field.getFieldName().equals(fieldName))
            .map(ClientCustomField::getFieldValue)
            .findFirst()
            .orElse(null);
    }
    
    // Set custom field
    public void setCustomFieldValue(String fieldName, String fieldValue) {
        ClientCustomField existingField = customFields.stream()
            .filter(field -> field.getFieldName().equals(fieldName))
            .findFirst()
            .orElse(null);
        
        if (existingField != null) {
            existingField.setFieldValue(fieldValue);
        } else {
            ClientCustomField newField = new ClientCustomField(fieldName, fieldValue);
            addCustomField(newField);
        }
    }
    
    // Business logic methods
    public boolean hasContacts() {
        return contacts != null && !contacts.isEmpty();
    }
    
    public boolean hasAddresses() {
        return addresses != null && !addresses.isEmpty();
    }
    
    public boolean hasCustomFields() {
        return customFields != null && !customFields.isEmpty();
    }
    
    public int getContactsCount() {
        return contacts != null ? contacts.size() : 0;
    }
    
    public int getAddressesCount() {
        return addresses != null ? addresses.size() : 0;
    }
    
    public int getCustomFieldsCount() {
        return customFields != null ? customFields.size() : 0;
    }
    
    // toString method
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", industry='" + industry + '\'' +
                ", status=" + status +
                ", contactsCount=" + getContactsCount() +
                ", addressesCount=" + getAddressesCount() +
                '}';
    }
}