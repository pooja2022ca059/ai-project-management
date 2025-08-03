package com.aipm.ai_project_management.modules.clients.repository;

import com.aipm.ai_project_management.modules.clients.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    // Find by email
    Optional<Client> findByEmail(String email);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find by status
    List<Client> findByStatus(Client.ClientStatus status);
    
    // Find by industry
    List<Client> findByIndustry(String industry);
    
    // Search clients by name (case insensitive)
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Client> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Search clients with pagination and filters
    @Query("SELECT c FROM Client c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:industry IS NULL OR c.industry = :industry) AND " +
           "(:status IS NULL OR c.status = :status)")
    Page<Client> findClientsWithFilters(
        @Param("name") String name,
        @Param("email") String email,
        @Param("industry") String industry,
        @Param("status") Client.ClientStatus status,
        Pageable pageable
    );
    
    // Count clients by status
    @Query("SELECT COUNT(c) FROM Client c WHERE c.status = :status")
    long countByStatus(@Param("status") Client.ClientStatus status);
    
    // Get active clients count
    @Query("SELECT COUNT(c) FROM Client c WHERE c.status = 'ACTIVE'")
    long countActiveClients();
    
    // Find clients with contacts
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.contacts WHERE c.id = :id")
    Optional<Client> findByIdWithContacts(@Param("id") Long id);
    
    // Find clients with addresses
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.addresses WHERE c.id = :id")
    Optional<Client> findByIdWithAddresses(@Param("id") Long id);
    
    // Find clients with all relations
    @Query("SELECT DISTINCT c FROM Client c " +
           "LEFT JOIN FETCH c.contacts " +
           "LEFT JOIN FETCH c.addresses " +
           "LEFT JOIN FETCH c.customFields " +
           "WHERE c.id = :id")
    Optional<Client> findByIdWithAllRelations(@Param("id") Long id);
    
    // Find recent clients (created in last N days)
    @Query("SELECT c FROM Client c WHERE c.createdAt >= :date ORDER BY c.createdAt DESC")
    List<Client> findRecentClients(@Param("date") java.time.LocalDateTime date);
    
    // Search clients globally
    @Query("SELECT c FROM Client c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.industry) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Client> globalSearch(@Param("search") String search, Pageable pageable);
}
