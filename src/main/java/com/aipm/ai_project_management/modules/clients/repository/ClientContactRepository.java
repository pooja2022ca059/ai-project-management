package com.aipm.ai_project_management.modules.clients.repository;

import com.aipm.ai_project_management.modules.clients.entity.ClientContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientContactRepository extends JpaRepository<ClientContact, Long> {
    
    // Find contacts by client ID
    List<ClientContact> findByClientId(Long clientId);
    
    // Find contact by email
    Optional<ClientContact> findByEmail(String email);
    
    // Find primary contact for client
    @Query("SELECT cc FROM ClientContact cc WHERE cc.client.id = :clientId AND cc.isPrimary = true")
    Optional<ClientContact> findPrimaryContactByClientId(@Param("clientId") Long clientId);
    
    // Set all contacts as non-primary for a client
    @Modifying
    @Query("UPDATE ClientContact cc SET cc.isPrimary = false WHERE cc.client.id = :clientId")
    void setAllContactsAsNonPrimary(@Param("clientId") Long clientId);
    
    // Delete all contacts for a client
    void deleteByClientId(Long clientId);
    
    // Count contacts for a client
    long countByClientId(Long clientId);
    
    // Check if client has primary contact
    @Query("SELECT COUNT(cc) > 0 FROM ClientContact cc WHERE cc.client.id = :clientId AND cc.isPrimary = true")
    boolean hasPrimaryContact(@Param("clientId") Long clientId);
    
    // Search contacts by name or email
    @Query("SELECT cc FROM ClientContact cc WHERE cc.client.id = :clientId AND " +
           "(LOWER(cc.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cc.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<ClientContact> searchContactsByClientId(@Param("clientId") Long clientId, @Param("search") String search);
    
    // Check if email exists for different client
    @Query("SELECT COUNT(cc) > 0 FROM ClientContact cc WHERE cc.email = :email AND cc.client.id != :clientId")
    boolean existsByEmailAndClientIdNot(@Param("email") String email, @Param("clientId") Long clientId);
}