package com.aipm.ai_project_management.modules.clients.service;

import com.aipm.ai_project_management.modules.clients.dto.*;
import com.aipm.ai_project_management.modules.clients.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    
    // CRUD Operations
    ClientDTO createClient(CreateClientRequest request);
    ClientDTO getClientById(Long id);
    ClientDTO updateClient(Long id, UpdateClientRequest request);
    void deleteClient(Long id);
    
    // List and Search Operations
    Page<ClientDTO> getAllClients(Pageable pageable);
    Page<ClientDTO> searchClients(String name, String email, String industry, 
                                  Client.ClientStatus status, Pageable pageable);
    Page<ClientDTO> globalSearchClients(String search, Pageable pageable);
    
    // Business Operations
    List<ClientDTO> getActiveClients();
    List<ClientDTO> getClientsByIndustry(String industry);
    List<ClientDTO> getRecentClients(int days);
    
    // Statistics
    long getTotalClientsCount();
    long getActiveClientsCount();
    long getClientsByStatusCount(Client.ClientStatus status);
    
    // Contact Management
    ClientDTO addContactToClient(Long clientId, ClientContactDTO contactDTO);
    ClientDTO updateClientContact(Long clientId, Long contactId, ClientContactDTO contactDTO);
    ClientDTO removeContactFromClient(Long clientId, Long contactId);
    
    // Address Management
    ClientDTO addAddressToClient(Long clientId, ClientAddressDTO addressDTO);
    ClientDTO updateClientAddress(Long clientId, Long addressId, ClientAddressDTO addressDTO);
    ClientDTO removeAddressFromClient(Long clientId, Long addressId);
    
    // Custom Fields Management
    ClientDTO addCustomFieldToClient(Long clientId, String fieldName, String fieldValue);
    ClientDTO updateClientCustomField(Long clientId, String fieldName, String fieldValue);
    ClientDTO removeCustomFieldFromClient(Long clientId, String fieldName);
    
    // Utility Methods
    boolean existsByEmail(String email);
    ClientDTO getClientWithAllRelations(Long id);
}
