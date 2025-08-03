package com.aipm.ai_project_management.modules.clients.repository;

import com.aipm.ai_project_management.modules.clients.entity.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientAddressRepository extends JpaRepository<ClientAddress, Long> {
    
    // Find addresses by client ID
    List<ClientAddress> findByClientId(Long clientId);
    
    // Find primary address for client
    @Query("SELECT ca FROM ClientAddress ca WHERE ca.client.id = :clientId AND ca.isPrimary = true")
    Optional<ClientAddress> findPrimaryAddressByClientId(@Param("clientId") Long clientId);
    
    // Set all addresses as non-primary for a client
    @Modifying
    @Query("UPDATE ClientAddress ca SET ca.isPrimary = false WHERE ca.client.id = :clientId")
    void setAllAddressesAsNonPrimary(@Param("clientId") Long clientId);
    
    // Delete all addresses for a client
    void deleteByClientId(Long clientId);
    
    // Count addresses for a client
    long countByClientId(Long clientId);
    
    // Check if client has primary address
    @Query("SELECT COUNT(ca) > 0 FROM ClientAddress ca WHERE ca.client.id = :clientId AND ca.isPrimary = true")
    boolean hasPrimaryAddress(@Param("clientId") Long clientId);
}
