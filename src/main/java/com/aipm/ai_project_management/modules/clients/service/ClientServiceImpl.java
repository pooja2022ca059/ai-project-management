package com.aipm.ai_project_management.modules.clients.service;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.clients.dto.*;
import com.aipm.ai_project_management.modules.clients.entity.*;
import com.aipm.ai_project_management.modules.clients.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private ClientContactRepository clientContactRepository;
    
    @Autowired
    private ClientAddressRepository clientAddressRepository;
    
    @Autowired
    private ClientCustomFieldRepository clientCustomFieldRepository;
    
    @Override
    public ClientDTO createClient(CreateClientRequest request) {
        logger.info("Creating new client with name: {}", request.getName());
        
        // Check if email already exists
        if (request.getEmail() != null && clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Client with email " + request.getEmail() + " already exists");
        }
        
        // Create client entity
        Client client = new Client();
        mapCreateRequestToEntity(request, client);
        
        // Save client
        Client savedClient = clientRepository.save(client);
        logger.info("Created client with ID: {}", savedClient.getId());
        
        // Add contacts if provided
        if (request.getContacts() != null && !request.getContacts().isEmpty()) {
            addContactsToClient(savedClient, request.getContacts());
        }
        
        // Add addresses if provided
        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            addAddressesToClient(savedClient, request.getAddresses());
        }
        
        // Add custom fields if provided
        if (request.getCustomFields() != null && !request.getCustomFields().isEmpty()) {
            addCustomFieldsToClient(savedClient, request.getCustomFields());
        }
        
        return mapEntityToDTO(savedClient, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        logger.info("Fetching client with ID: {}", id);
        
        Client client = clientRepository.findByIdWithAllRelations(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO updateClient(Long id, UpdateClientRequest request) {
        logger.info("Updating client with ID: {}", id);
        
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        // Check email uniqueness if email is being updated
        if (request.getEmail() != null && !request.getEmail().equals(client.getEmail())) {
            if (clientRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Client with email " + request.getEmail() + " already exists");
            }
        }
        
        // Update basic fields
        mapUpdateRequestToEntity(request, client);
        
        // Save client
        Client updatedClient = clientRepository.save(client);
        
        // Update contacts if provided
        if (request.getContacts() != null) {
            updateClientContacts(updatedClient, request.getContacts());
        }
        
        // Update addresses if provided
        if (request.getAddresses() != null) {
            updateClientAddresses(updatedClient, request.getAddresses());
        }
        
        // Update custom fields if provided
        if (request.getCustomFields() != null) {
            updateClientCustomFields(updatedClient, request.getCustomFields());
        }
        
        logger.info("Updated client with ID: {}", id);
        return mapEntityToDTO(updatedClient, true);
    }
    
    @Override
    public void deleteClient(Long id) {
        logger.info("Deleting client with ID: {}", id);
        
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        clientRepository.delete(client);
        logger.info("Deleted client with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> getAllClients(Pageable pageable) {
        logger.info("Fetching all clients with pagination");
        
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(client -> mapEntityToDTO(client, false));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> searchClients(String name, String email, String industry, 
                                        Client.ClientStatus status, Pageable pageable) {
        logger.info("Searching clients with filters: name={}, email={}, industry={}, status={}", 
                   name, email, industry, status);
        
        Page<Client> clients = clientRepository.findClientsWithFilters(name, email, industry, status, pageable);
        return clients.map(client -> mapEntityToDTO(client, false));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> globalSearchClients(String search, Pageable pageable) {
        logger.info("Global search for clients with term: {}", search);
        
        Page<Client> clients = clientRepository.globalSearch(search, pageable);
        return clients.map(client -> mapEntityToDTO(client, false));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getActiveClients() {
        logger.info("Fetching active clients");
        
        List<Client> clients = clientRepository.findByStatus(Client.ClientStatus.ACTIVE);
        return clients.stream()
            .map(client -> mapEntityToDTO(client, false))
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getClientsByIndustry(String industry) {
        logger.info("Fetching clients by industry: {}", industry);
        
        List<Client> clients = clientRepository.findByIndustry(industry);
        return clients.stream()
            .map(client -> mapEntityToDTO(client, false))
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getRecentClients(int days) {
        logger.info("Fetching clients created in last {} days", days);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        List<Client> clients = clientRepository.findRecentClients(cutoffDate);
        return clients.stream()
            .map(client -> mapEntityToDTO(client, false))
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalClientsCount() {
        return clientRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getActiveClientsCount() {
        return clientRepository.countActiveClients();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getClientsByStatusCount(Client.ClientStatus status) {
        return clientRepository.countByStatus(status);
    }
    
    @Override
    public ClientDTO addContactToClient(Long clientId, ClientContactDTO contactDTO) {
        logger.info("Adding contact to client ID: {}", clientId);
        
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        
        ClientContact contact = mapContactDTOToEntity(contactDTO);
        client.addContact(contact);
        
        // If this is marked as primary, unset other primary contacts
        if (Boolean.TRUE.equals(contactDTO.getIsPrimary())) {
            clientContactRepository.setAllContactsAsNonPrimary(clientId);
            contact.setIsPrimary(true);
        }
        
        clientRepository.save(client);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO updateClientContact(Long clientId, Long contactId, ClientContactDTO contactDTO) {
        logger.info("Updating contact ID: {} for client ID: {}", contactId, clientId);
        
        ClientContact contact = clientContactRepository.findById(contactId)
            .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + contactId));
        
        if (!contact.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Contact does not belong to the specified client");
        }
        
        // Update contact fields
        mapContactDTOToEntity(contactDTO, contact);
        
        // Handle primary contact logic
        if (Boolean.TRUE.equals(contactDTO.getIsPrimary())) {
            clientContactRepository.setAllContactsAsNonPrimary(clientId);
            contact.setIsPrimary(true);
        }
        
        clientContactRepository.save(contact);
        
        Client client = clientRepository.findByIdWithAllRelations(clientId).orElse(null);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO removeContactFromClient(Long clientId, Long contactId) {
        logger.info("Removing contact ID: {} from client ID: {}", contactId, clientId);
        
        ClientContact contact = clientContactRepository.findById(contactId)
            .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + contactId));
        
        if (!contact.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Contact does not belong to the specified client");
        }
        
        clientContactRepository.delete(contact);
        
        Client client = clientRepository.findByIdWithAllRelations(clientId).orElse(null);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO addAddressToClient(Long clientId, ClientAddressDTO addressDTO) {
        logger.info("Adding address to client ID: {}", clientId);
        
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        
        ClientAddress address = mapAddressDTOToEntity(addressDTO);
        client.addAddress(address);
        
        // If this is marked as primary, unset other primary addresses
        if (Boolean.TRUE.equals(addressDTO.getIsPrimary())) {
            clientAddressRepository.setAllAddressesAsNonPrimary(clientId);
            address.setIsPrimary(true);
        }
        
        clientRepository.save(client);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO updateClientAddress(Long clientId, Long addressId, ClientAddressDTO addressDTO) {
        logger.info("Updating address ID: {} for client ID: {}", addressId, clientId);
        
        ClientAddress address = clientAddressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        
        if (!address.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Address does not belong to the specified client");
        }
        
        // Update address fields
        mapAddressDTOToEntity(addressDTO, address);
        
        // Handle primary address logic
        if (Boolean.TRUE.equals(addressDTO.getIsPrimary())) {
            clientAddressRepository.setAllAddressesAsNonPrimary(clientId);
            address.setIsPrimary(true);
        }
        
        clientAddressRepository.save(address);
        
        Client client = clientRepository.findByIdWithAllRelations(clientId).orElse(null);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO removeAddressFromClient(Long clientId, Long addressId) {
        logger.info("Removing address ID: {} from client ID: {}", addressId, clientId);
        
        ClientAddress address = clientAddressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        
        if (!address.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Address does not belong to the specified client");
        }
        
        clientAddressRepository.delete(address);
        
        Client client = clientRepository.findByIdWithAllRelations(clientId).orElse(null);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO addCustomFieldToClient(Long clientId, String fieldName, String fieldValue) {
        logger.info("Adding custom field '{}' to client ID: {}", fieldName, clientId);
        
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
        
        ClientCustomField customField = new ClientCustomField(fieldName, fieldValue);
        client.addCustomField(customField);
        
        clientRepository.save(client);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO updateClientCustomField(Long clientId, String fieldName, String fieldValue) {
        logger.info("Updating custom field '{}' for client ID: {}", fieldName, clientId);
        
        ClientCustomField customField = clientCustomFieldRepository.findByClientIdAndFieldName(clientId, fieldName)
            .orElseThrow(() -> new ResourceNotFoundException("Custom field not found: " + fieldName));
        
        customField.setFieldValue(fieldValue);
        clientCustomFieldRepository.save(customField);
        
        Client client = clientRepository.findByIdWithAllRelations(clientId).orElse(null);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    public ClientDTO removeCustomFieldFromClient(Long clientId, String fieldName) {
        logger.info("Removing custom field '{}' from client ID: {}", fieldName, clientId);
        
        ClientCustomField customField = clientCustomFieldRepository.findByClientIdAndFieldName(clientId, fieldName)
            .orElseThrow(() -> new ResourceNotFoundException("Custom field not found: " + fieldName));
        
        clientCustomFieldRepository.delete(customField);
        
        Client client = clientRepository.findByIdWithAllRelations(clientId).orElse(null);
        return mapEntityToDTO(client, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClientWithAllRelations(Long id) {
        logger.info("Fetching client with all relations for ID: {}", id);
        
        Client client = clientRepository.findByIdWithAllRelations(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        return mapEntityToDTO(client, true);
    }
    
    // Private mapping methods will be provided in the next part...
// ==================== PRIVATE MAPPING METHODS ====================
    
    private void mapCreateRequestToEntity(CreateClientRequest request, Client client) {
        client.setName(request.getName());
        client.setLogoUrl(request.getLogoUrl());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setWebsite(request.getWebsite());
        client.setIndustry(request.getIndustry());
        client.setCompanySize(request.getCompanySize());
        client.setStatus(Client.ClientStatus.ACTIVE);
    }
    
    private void mapUpdateRequestToEntity(UpdateClientRequest request, Client client) {
        if (request.getName() != null) {
            client.setName(request.getName());
        }
        if (request.getLogoUrl() != null) {
            client.setLogoUrl(request.getLogoUrl());
        }
        if (request.getEmail() != null) {
            client.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            client.setPhone(request.getPhone());
        }
        if (request.getWebsite() != null) {
            client.setWebsite(request.getWebsite());
        }
        if (request.getIndustry() != null) {
            client.setIndustry(request.getIndustry());
        }
        if (request.getCompanySize() != null) {
            client.setCompanySize(request.getCompanySize());
        }
        if (request.getStatus() != null) {
            client.setStatus(request.getStatus());
        }
    }
    
    private ClientDTO mapEntityToDTO(Client client, boolean includeRelations) {
        ClientDTO dto = new ClientDTO();
        
        // Basic fields
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setLogoUrl(client.getLogoUrl());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setWebsite(client.getWebsite());
        dto.setIndustry(client.getIndustry());
        dto.setCompanySize(client.getCompanySize());
        dto.setStatus(client.getStatus());
        
        // Audit fields
        dto.setCreatedAt(client.getCreatedAt());
        dto.setUpdatedAt(client.getUpdatedAt());
        dto.setCreatedBy(client.getCreatedBy() != null ? client.getCreatedBy().toString() : null);
        dto.setUpdatedBy(client.getUpdatedBy() != null ? client.getUpdatedBy().toString() : null);
        
        // Counts
        dto.setContactsCount(client.getContactsCount());
        dto.setAddressesCount(client.getAddressesCount());
        dto.setCustomFieldsCount(client.getCustomFieldsCount());
        
        if (includeRelations) {
            // Map contacts
            if (client.getContacts() != null) {
                dto.setContacts(client.getContacts().stream()
                    .map(this::mapContactEntityToDTO)
                    .collect(Collectors.toList()));
            }
            
            // Map addresses
            if (client.getAddresses() != null) {
                dto.setAddresses(client.getAddresses().stream()
                    .map(this::mapAddressEntityToDTO)
                    .collect(Collectors.toList()));
            }
            
            // Map custom fields
            if (client.getCustomFields() != null) {
                Map<String, String> customFieldsMap = new HashMap<>();
                client.getCustomFields().forEach(field -> 
                    customFieldsMap.put(field.getFieldName(), field.getFieldValue()));
                dto.setCustomFields(customFieldsMap);
            }
        }
        
        return dto;
    }
    
    private ClientContactDTO mapContactEntityToDTO(ClientContact contact) {
        ClientContactDTO dto = new ClientContactDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setPhone(contact.getPhone());
        dto.setRole(contact.getRole());
        dto.setIsPrimary(contact.getIsPrimary());
        dto.setCreatedAt(contact.getCreatedAt());
        dto.setUpdatedAt(contact.getUpdatedAt());
        return dto;
    }
    
    private ClientAddressDTO mapAddressEntityToDTO(ClientAddress address) {
        ClientAddressDTO dto = new ClientAddressDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZip(address.getZip());
        dto.setCountry(address.getCountry());
        dto.setIsPrimary(address.getIsPrimary());
        dto.setCreatedAt(address.getCreatedAt());
        return dto;
    }
    
    private ClientContact mapContactDTOToEntity(ClientContactDTO dto) {
        ClientContact contact = new ClientContact();
        mapContactDTOToEntity(dto, contact);
        return contact;
    }
    
    private void mapContactDTOToEntity(ClientContactDTO dto, ClientContact contact) {
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setRole(dto.getRole());
        contact.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);
        contact.setUpdatedAt(LocalDateTime.now());
    }
    
    private ClientAddress mapAddressDTOToEntity(ClientAddressDTO dto) {
        ClientAddress address = new ClientAddress();
        mapAddressDTOToEntity(dto, address);
        return address;
    }
    
    private void mapAddressDTOToEntity(ClientAddressDTO dto, ClientAddress address) {
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZip(dto.getZip());
        address.setCountry(dto.getCountry());
        address.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);
    }
    
    private void addContactsToClient(Client client, List<ClientContactDTO> contactDTOs) {
        for (ClientContactDTO contactDTO : contactDTOs) {
            ClientContact contact = mapContactDTOToEntity(contactDTO);
            client.addContact(contact);
        }
    }
    
    private void addAddressesToClient(Client client, List<ClientAddressDTO> addressDTOs) {
        for (ClientAddressDTO addressDTO : addressDTOs) {
            ClientAddress address = mapAddressDTOToEntity(addressDTO);
            client.addAddress(address);
        }
    }
    
    private void addCustomFieldsToClient(Client client, Map<String, String> customFields) {
        for (Map.Entry<String, String> entry : customFields.entrySet()) {
            ClientCustomField customField = new ClientCustomField(entry.getKey(), entry.getValue());
            client.addCustomField(customField);
        }
    }
    
    private void updateClientContacts(Client client, List<ClientContactDTO> contactDTOs) {
        // Remove existing contacts
        client.getContacts().clear();
        clientContactRepository.deleteByClientId(client.getId());
        
        // Add new contacts
        addContactsToClient(client, contactDTOs);
    }
    
    private void updateClientAddresses(Client client, List<ClientAddressDTO> addressDTOs) {
        // Remove existing addresses
        client.getAddresses().clear();
        clientAddressRepository.deleteByClientId(client.getId());
        
        // Add new addresses
        addAddressesToClient(client, addressDTOs);
    }
    
    private void updateClientCustomFields(Client client, Map<String, String> customFields) {
        // Remove existing custom fields
        client.getCustomFields().clear();
        clientCustomFieldRepository.deleteByClientId(client.getId());
        
        // Add new custom fields
        addCustomFieldsToClient(client, customFields);
    }
}

