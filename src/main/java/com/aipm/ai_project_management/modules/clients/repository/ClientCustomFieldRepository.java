package com.aipm.ai_project_management.modules.clients.repository;

import com.aipm.ai_project_management.modules.clients.entity.ClientCustomField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientCustomFieldRepository extends JpaRepository<ClientCustomField, Long> {
    
    // Find custom fields by client ID
    List<ClientCustomField> findByClientId(Long clientId);
    
    // Find custom field by client ID and field name
    @Query("SELECT ccf FROM ClientCustomField ccf WHERE ccf.client.id = :clientId AND ccf.fieldName = :fieldName")
    Optional<ClientCustomField> findByClientIdAndFieldName(@Param("clientId") Long clientId, @Param("fieldName") String fieldName);
    
    // Delete all custom fields for a client
    void deleteByClientId(Long clientId);
    
    // Delete custom field by client ID and field name
    @Query("DELETE FROM ClientCustomField ccf WHERE ccf.client.id = :clientId AND ccf.fieldName = :fieldName")
    void deleteByClientIdAndFieldName(@Param("clientId") Long clientId, @Param("fieldName") String fieldName);
    
    // Count custom fields for a client
    long countByClientId(Long clientId);
    
    // Get all unique field names
    @Query("SELECT DISTINCT ccf.fieldName FROM ClientCustomField ccf")
    List<String> findAllUniqueFieldNames();
    
    // Find custom fields by field name across all clients
    List<ClientCustomField> findByFieldName(String fieldName);
    
    // Search custom fields by value
    @Query("SELECT ccf FROM ClientCustomField ccf WHERE ccf.client.id = :clientId AND " +
           "LOWER(ccf.fieldValue) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<ClientCustomField> searchCustomFieldsByClientId(@Param("clientId") Long clientId, @Param("search") String search);
}
