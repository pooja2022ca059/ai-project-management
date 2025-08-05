package com.aipm.ai_project_management.modules.ai.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.ai.dto.AiInsightDTO;
import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import com.aipm.ai_project_management.modules.ai.repository.AiInsightRepository;
import com.aipm.ai_project_management.modules.ai.service.AiInsightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AiInsightServiceImpl implements AiInsightService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiInsightServiceImpl.class);
    
    @Autowired
    private AiInsightRepository aiInsightRepository;
    
    @Override
    public AiInsightDTO createInsight(AiInsightDTO insightDTO) {
        logger.info("Creating new AI insight: {}", insightDTO.getTitle());
        
        AiInsightEntity entity = mapDTOToEntity(insightDTO);
        AiInsightEntity savedEntity = aiInsightRepository.save(entity);
        
        logger.info("Created AI insight with ID: {}", savedEntity.getId());
        return mapEntityToDTO(savedEntity);
    }
    
    @Override
    public AiInsightDTO updateInsight(Long id, AiInsightDTO insightDTO) {
        logger.info("Updating AI insight with ID: {}", id);
        
        AiInsightEntity entity = aiInsightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AI Insight not found with id: " + id));
        
        // Update fields
        entity.setTitle(insightDTO.getTitle());
        entity.setDescription(insightDTO.getDescription());
        entity.setInsightData(insightDTO.getInsightData());
        entity.setPriority(insightDTO.getPriority());
        entity.setStatus(insightDTO.getStatus());
        entity.setConfidenceScore(insightDTO.getConfidenceScore());
        entity.setExpiresAt(insightDTO.getExpiresAt());
        
        AiInsightEntity updatedEntity = aiInsightRepository.save(entity);
        logger.info("Updated AI insight with ID: {}", id);
        return mapEntityToDTO(updatedEntity);
    }
    
    @Override
    public void deleteInsight(Long id) {
        logger.info("Deleting AI insight with ID: {}", id);
        
        if (!aiInsightRepository.existsById(id)) {
            throw new ResourceNotFoundException("AI Insight not found with id: " + id);
        }
        
        aiInsightRepository.deleteById(id);
        logger.info("Deleted AI insight with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AiInsightDTO getInsightById(Long id) {
        logger.info("Fetching AI insight with ID: {}", id);
        
        AiInsightEntity entity = aiInsightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AI Insight not found with id: " + id));
        
        return mapEntityToDTO(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getAllActiveInsights() {
        logger.info("Fetching all active AI insights");
        
        List<AiInsightEntity> entities = aiInsightRepository.findByStatus(AiInsightEntity.InsightStatus.ACTIVE);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getInsightsByType(AiInsightEntity.InsightType type) {
        logger.info("Fetching insights by type: {}", type);
        
        List<AiInsightEntity> entities = aiInsightRepository.findByType(type);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getInsightsByPriority(AiInsightEntity.InsightPriority priority) {
        logger.info("Fetching insights by priority: {}", priority);
        
        List<AiInsightEntity> entities = aiInsightRepository.findByPriority(priority);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getInsightsForEntity(String entityType, Long entityId) {
        logger.info("Fetching insights for entity: {} with ID: {}", entityType, entityId);
        
        List<AiInsightEntity> entities = aiInsightRepository.findActiveInsightsForEntity(entityType, entityId);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getHighPriorityInsights() {
        logger.info("Fetching high priority insights");
        
        List<AiInsightEntity> entities = aiInsightRepository.findHighPriorityActiveInsights();
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getInsightsNeedingAction() {
        logger.info("Fetching insights needing action");
        
        List<AiInsightEntity> entities = aiInsightRepository.findInsightsNeedingAction();
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AiInsightDTO> getInsightsByStatus(AiInsightEntity.InsightStatus status, Pageable pageable) {
        logger.info("Fetching insights by status: {} with pagination", status);
        
        Page<AiInsightEntity> entities = aiInsightRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return entities.map(this::mapEntityToDTO);
    }
    
    @Override
    public AiInsightDTO markInsightActionTaken(Long id, Long actionTakenBy) {
        logger.info("Marking insight {} as action taken by user {}", id, actionTakenBy);
        
        AiInsightEntity entity = aiInsightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AI Insight not found with id: " + id));
        
        entity.setActionTaken(true);
        entity.setActionTakenAt(LocalDateTime.now());
        entity.setActionTakenBy(actionTakenBy);
        entity.setStatus(AiInsightEntity.InsightStatus.IMPLEMENTED);
        
        AiInsightEntity updatedEntity = aiInsightRepository.save(entity);
        return mapEntityToDTO(updatedEntity);
    }
    
    @Override
    public AiInsightDTO dismissInsight(Long id, Long dismissedBy) {
        logger.info("Dismissing insight {} by user {}", id, dismissedBy);
        
        AiInsightEntity entity = aiInsightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AI Insight not found with id: " + id));
        
        entity.setStatus(AiInsightEntity.InsightStatus.DISMISSED);
        entity.setActionTakenBy(dismissedBy);
        entity.setActionTakenAt(LocalDateTime.now());
        
        AiInsightEntity updatedEntity = aiInsightRepository.save(entity);
        return mapEntityToDTO(updatedEntity);
    }
    
    @Override
    public void expireOldInsights() {
        logger.info("Expiring old insights");
        
        List<AiInsightEntity> expiredInsights = aiInsightRepository.findExpiredInsights(LocalDateTime.now());
        
        for (AiInsightEntity insight : expiredInsights) {
            insight.setStatus(AiInsightEntity.InsightStatus.EXPIRED);
        }
        
        aiInsightRepository.saveAll(expiredInsights);
        logger.info("Expired {} insights", expiredInsights.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getInsightCount() {
        return aiInsightRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getInsightCountByType(AiInsightEntity.InsightType type) {
        return aiInsightRepository.countByType(type);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getInsightCountByStatus(AiInsightEntity.InsightStatus status) {
        return aiInsightRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getRecentInsights(int days) {
        logger.info("Fetching insights from last {} days", days);
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<AiInsightEntity> entities = aiInsightRepository.findRecentInsights(since);
        return entities.stream()
            .map(this::mapEntityToDTO)
            .collect(Collectors.toList());
    }
    
    // AI-powered insight generation methods (simplified implementations)
    @Override
    public List<AiInsightDTO> generateProjectInsights(Long projectId) {
        logger.info("Generating AI insights for project: {}", projectId);
        
        // This is a simplified implementation - in real scenario, this would involve
        // complex AI analysis of project data, progress, risks, etc.
        AiInsightDTO insight = new AiInsightDTO();
        insight.setType(AiInsightEntity.InsightType.PROJECT_RISK_ALERT);
        insight.setTitle("Project Analysis Complete");
        insight.setDescription("Based on current progress and resource allocation, project is on track.");
        insight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        insight.setRelatedEntityType("PROJECT");
        insight.setRelatedEntityId(projectId);
        insight.setGeneratedBy("AI_ENGINE_V1");
        insight.setConfidenceScore(0.85);
        
        AiInsightDTO createdInsight = createInsight(insight);
        return List.of(createdInsight);
    }
    
    @Override
    public List<AiInsightDTO> generateTaskInsights(Long taskId) {
        logger.info("Generating AI insights for task: {}", taskId);
        
        AiInsightDTO insight = new AiInsightDTO();
        insight.setType(AiInsightEntity.InsightType.TASK_RECOMMENDATION);
        insight.setTitle("Task Optimization Suggestion");
        insight.setDescription("Consider breaking down this task into smaller subtasks for better tracking.");
        insight.setPriority(AiInsightEntity.InsightPriority.LOW);
        insight.setRelatedEntityType("TASK");
        insight.setRelatedEntityId(taskId);
        insight.setGeneratedBy("AI_ENGINE_V1");
        insight.setConfidenceScore(0.75);
        
        AiInsightDTO createdInsight = createInsight(insight);
        return List.of(createdInsight);
    }
    
    @Override
    public List<AiInsightDTO> generateTeamInsights(Long teamId) {
        logger.info("Generating AI insights for team: {}", teamId);
        
        AiInsightDTO insight = new AiInsightDTO();
        insight.setType(AiInsightEntity.InsightType.TEAM_PRODUCTIVITY);
        insight.setTitle("Team Performance Analysis");
        insight.setDescription("Team productivity is above average. Consider implementing knowledge sharing sessions.");
        insight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        insight.setRelatedEntityType("TEAM");
        insight.setRelatedEntityId(teamId);
        insight.setGeneratedBy("AI_ENGINE_V1");
        insight.setConfidenceScore(0.80);
        
        AiInsightDTO createdInsight = createInsight(insight);
        return List.of(createdInsight);
    }
    
    @Override
    public AiInsightDTO generateBudgetInsight(Long projectId) {
        logger.info("Generating budget insight for project: {}", projectId);
        
        AiInsightDTO insight = new AiInsightDTO();
        insight.setType(AiInsightEntity.InsightType.BUDGET_FORECAST);
        insight.setTitle("Budget Analysis");
        insight.setDescription("Current spending is within budget. Projected completion cost: 95% of allocated budget.");
        insight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        insight.setRelatedEntityType("PROJECT");
        insight.setRelatedEntityId(projectId);
        insight.setGeneratedBy("AI_ENGINE_V1");
        insight.setConfidenceScore(0.90);
        
        return createInsight(insight);
    }
    
    @Override
    public AiInsightDTO generateDeadlineInsight(Long projectId) {
        logger.info("Generating deadline insight for project: {}", projectId);
        
        AiInsightDTO insight = new AiInsightDTO();
        insight.setType(AiInsightEntity.InsightType.DEADLINE_PREDICTION);
        insight.setTitle("Deadline Forecast");
        insight.setDescription("Based on current velocity, project will complete 2 days ahead of schedule.");
        insight.setPriority(AiInsightEntity.InsightPriority.HIGH);
        insight.setRelatedEntityType("PROJECT");
        insight.setRelatedEntityId(projectId);
        insight.setGeneratedBy("AI_ENGINE_V1");
        insight.setConfidenceScore(0.88);
        
        return createInsight(insight);
    }
    
    // Private mapping methods
    private AiInsightEntity mapDTOToEntity(AiInsightDTO dto) {
        AiInsightEntity entity = new AiInsightEntity();
        entity.setType(dto.getType());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setInsightData(dto.getInsightData());
        entity.setPriority(dto.getPriority() != null ? dto.getPriority() : AiInsightEntity.InsightPriority.MEDIUM);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : AiInsightEntity.InsightStatus.ACTIVE);
        entity.setConfidenceScore(dto.getConfidenceScore());
        entity.setRelatedEntityType(dto.getRelatedEntityType());
        entity.setRelatedEntityId(dto.getRelatedEntityId());
        entity.setGeneratedBy(dto.getGeneratedBy());
        entity.setExpiresAt(dto.getExpiresAt());
        entity.setActionTaken(dto.getActionTaken() != null ? dto.getActionTaken() : false);
        return entity;
    }
    
    private AiInsightDTO mapEntityToDTO(AiInsightEntity entity) {
        AiInsightDTO dto = new AiInsightDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setInsightData(entity.getInsightData());
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setConfidenceScore(entity.getConfidenceScore());
        dto.setRelatedEntityType(entity.getRelatedEntityType());
        dto.setRelatedEntityId(entity.getRelatedEntityId());
        dto.setGeneratedBy(entity.getGeneratedBy());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setActionTaken(entity.getActionTaken());
        dto.setActionTakenAt(entity.getActionTakenAt());
        dto.setActionTakenBy(entity.getActionTakenBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}