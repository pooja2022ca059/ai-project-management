package com.aipm.ai_project_management.modules.clients.controller;

import com.aipm.ai_project_management.common.response.ApiResponse;
import com.aipm.ai_project_management.modules.clients.dto.*;
import com.aipm.ai_project_management.modules.clients.entity.Client;
import com.aipm.ai_project_management.modules.clients.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client Management", description = "APIs for managing clients")
public class ClientController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    
    @Autowired
    private ClientService clientService;
    
    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client with the provided information")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> createClient(@Valid @RequestBody CreateClientRequest request) {
        logger.info("Creating new client: {}", request.getName());
        
        try {
            ClientDTO clientDTO = clientService.createClient(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Client created successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error creating client: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to create client: " + e.getMessage()));
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieves a paginated list of all clients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<ClientDTO>>> getAllClients(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Fetching all clients - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<ClientDTO> clients = clientService.getAllClients(pageable);
            return ResponseEntity.ok(ApiResponse.success("Clients retrieved successfully", clients));
        } catch (Exception e) {
            logger.error("Error fetching clients: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch clients: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Retrieves a specific client by their ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<ClientDTO>> getClientById(@PathVariable Long id) {
        logger.info("Fetching client with ID: {}", id);
        
        try {
            ClientDTO clientDTO = clientService.getClientById(id);
            return ResponseEntity.ok(ApiResponse.success("Client retrieved successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error fetching client with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Client not found: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update client", description = "Updates an existing client with the provided information")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> updateClient(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateClientRequest request) {
        
        logger.info("Updating client with ID: {}", id);
        
        try {
            ClientDTO clientDTO = clientService.updateClient(id, request);
            return ResponseEntity.ok(ApiResponse.success("Client updated successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error updating client with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update client: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client", description = "Deletes a client by their ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Long id) {
        logger.info("Deleting client with ID: {}", id);
        
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok(ApiResponse.success("Client deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting client with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Failed to delete client: " + e.getMessage()));
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search clients", description = "Search clients with filters")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<ClientDTO>>> searchClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) Client.ClientStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Searching clients with filters");
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<ClientDTO> clients = clientService.searchClients(name, email, industry, status, pageable);
            return ResponseEntity.ok(ApiResponse.success("Search completed successfully", clients));
        } catch (Exception e) {
            logger.error("Error searching clients: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Search failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/global-search")
    @Operation(summary = "Global search clients", description = "Global search across client fields")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<ClientDTO>>> globalSearchClients(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Global search for: {}", search);
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            Page<ClientDTO> clients = clientService.globalSearchClients(search, pageable);
            return ResponseEntity.ok(ApiResponse.success("Global search completed successfully", clients));
        } catch (Exception e) {
            logger.error("Error in global search: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Global search failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active clients", description = "Retrieves all active clients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<List<ClientDTO>>> getActiveClients() {
        logger.info("Fetching active clients");
        
        try {
            List<ClientDTO> clients = clientService.getActiveClients();
            return ResponseEntity.ok(ApiResponse.success("Active clients retrieved successfully", clients));
        } catch (Exception e) {
            logger.error("Error fetching active clients: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch active clients: " + e.getMessage()));
        }
    }
    
    @GetMapping("/industry/{industry}")
    @Operation(summary = "Get clients by industry", description = "Retrieves clients filtered by industry")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<List<ClientDTO>>> getClientsByIndustry(@PathVariable String industry) {
        logger.info("Fetching clients for industry: {}", industry);
        
        try {
            List<ClientDTO> clients = clientService.getClientsByIndustry(industry);
            return ResponseEntity.ok(ApiResponse.success("Clients retrieved successfully", clients));
        } catch (Exception e) {
            logger.error("Error fetching clients by industry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch clients: " + e.getMessage()));
        }
    }
    
    @GetMapping("/recent")
    @Operation(summary = "Get recent clients", description = "Retrieves recently created clients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<ClientDTO>>> getRecentClients(
            @RequestParam(defaultValue = "30") int days) {
        
        logger.info("Fetching clients created in last {} days", days);
        
        try {
            List<ClientDTO> clients = clientService.getRecentClients(days);
            return ResponseEntity.ok(ApiResponse.success("Recent clients retrieved successfully", clients));
        } catch (Exception e) {
            logger.error("Error fetching recent clients: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch recent clients: " + e.getMessage()));
        }
    }
    
    @GetMapping("/stats")
    @Operation(summary = "Get client statistics", description = "Retrieves client statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getClientStats() {
        logger.info("Fetching client statistics");
        
        try {
            Map<String, Long> stats = Map.of(
                "total", clientService.getTotalClientsCount(),
                "active", clientService.getActiveClientsCount(),
                "inactive", clientService.getClientsByStatusCount(Client.ClientStatus.INACTIVE),
                "suspended", clientService.getClientsByStatusCount(Client.ClientStatus.SUSPENDED)
            );
            
            return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", stats));
        } catch (Exception e) {
            logger.error("Error fetching client statistics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to fetch statistics: " + e.getMessage()));
        }
    }
    
    // Contact Management Endpoints
    @PostMapping("/{id}/contacts")
    @Operation(summary = "Add contact to client", description = "Adds a new contact to the specified client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> addContactToClient(
            @PathVariable Long id, 
            @Valid @RequestBody ClientContactDTO contactDTO) {
        
        logger.info("Adding contact to client ID: {}", id);
        
        try {
            ClientDTO clientDTO = clientService.addContactToClient(id, contactDTO);
            return ResponseEntity.ok(ApiResponse.success("Contact added successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error adding contact to client: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to add contact: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{clientId}/contacts/{contactId}")
    @Operation(summary = "Update client contact", description = "Updates a specific contact for the client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> updateClientContact(
            @PathVariable Long clientId,
            @PathVariable Long contactId,
            @Valid @RequestBody ClientContactDTO contactDTO) {
        
        logger.info("Updating contact ID: {} for client ID: {}", contactId, clientId);
        
        try {
            ClientDTO clientDTO = clientService.updateClientContact(clientId, contactId, contactDTO);
            return ResponseEntity.ok(ApiResponse.success("Contact updated successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error updating contact: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update contact: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{clientId}/contacts/{contactId}")
    @Operation(summary = "Remove contact from client", description = "Removes a contact from the specified client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> removeContactFromClient(
            @PathVariable Long clientId,
            @PathVariable Long contactId) {
        
        logger.info("Removing contact ID: {} from client ID: {}", contactId, clientId);
        
        try {
            ClientDTO clientDTO = clientService.removeContactFromClient(clientId, contactId);
            return ResponseEntity.ok(ApiResponse.success("Contact removed successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error removing contact: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to remove contact: " + e.getMessage()));
        }
    }
    
    // Address Management Endpoints
    @PostMapping("/{id}/addresses")
    @Operation(summary = "Add address to client", description = "Adds a new address to the specified client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> addAddressToClient(
            @PathVariable Long id, 
            @Valid @RequestBody ClientAddressDTO addressDTO) {
        
        logger.info("Adding address to client ID: {}", id);
        
        try {
            ClientDTO clientDTO = clientService.addAddressToClient(id, addressDTO);
            return ResponseEntity.ok(ApiResponse.success("Address added successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error adding address to client: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to add address: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{clientId}/addresses/{addressId}")
    @Operation(summary = "Update client address", description = "Updates a specific address for the client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> updateClientAddress(
            @PathVariable Long clientId,
            @PathVariable Long addressId,
            @Valid @RequestBody ClientAddressDTO addressDTO) {
        
        logger.info("Updating address ID: {} for client ID: {}", addressId, clientId);
        
        try {
            ClientDTO clientDTO = clientService.updateClientAddress(clientId, addressId, addressDTO);
            return ResponseEntity.ok(ApiResponse.success("Address updated successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error updating address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update address: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{clientId}/addresses/{addressId}")
    @Operation(summary = "Remove address from client", description = "Removes an address from the specified client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> removeAddressFromClient(
            @PathVariable Long clientId,
            @PathVariable Long addressId) {
        
        logger.info("Removing address ID: {} from client ID: {}", addressId, clientId);
        
        try {
            ClientDTO clientDTO = clientService.removeAddressFromClient(clientId, addressId);
            return ResponseEntity.ok(ApiResponse.success("Address removed successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error removing address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to remove address: " + e.getMessage()));
        }
    }
    
    // Custom Fields Management
    @PostMapping("/{id}/custom-fields")
    @Operation(summary = "Add custom field to client", description = "Adds a custom field to the specified client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> addCustomFieldToClient(
            @PathVariable Long id,
            @RequestParam String fieldName,
            @RequestParam String fieldValue) {
        
        logger.info("Adding custom field '{}' to client ID: {}", fieldName, id);
        
        try {
            ClientDTO clientDTO = clientService.addCustomFieldToClient(id, fieldName, fieldValue);
            return ResponseEntity.ok(ApiResponse.success("Custom field added successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error adding custom field: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to add custom field: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/custom-fields/{fieldName}")
    @Operation(summary = "Update custom field", description = "Updates a custom field for the specified client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> updateCustomField(
            @PathVariable Long id,
            @PathVariable String fieldName,
            @RequestParam String fieldValue) {
        
        logger.info("Updating custom field '{}' for client ID: {}", fieldName, id);
        
        try {
            ClientDTO clientDTO = clientService.updateClientCustomField(id, fieldName, fieldValue);
            return ResponseEntity.ok(ApiResponse.success("Custom field updated successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error updating custom field: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update custom field: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}/custom-fields/{fieldName}")
    @Operation(summary = "Remove custom field", description = "Removes a custom field from the specified client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ClientDTO>> removeCustomField(
            @PathVariable Long id,
            @PathVariable String fieldName) {
        
        logger.info("Removing custom field '{}' from client ID: {}", fieldName, id);
        
        try {
            ClientDTO clientDTO = clientService.removeCustomFieldFromClient(id, fieldName);
            return ResponseEntity.ok(ApiResponse.success("Custom field removed successfully", clientDTO));
        } catch (Exception e) {
            logger.error("Error removing custom field: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to remove custom field: " + e.getMessage()));
        }
    }
}
