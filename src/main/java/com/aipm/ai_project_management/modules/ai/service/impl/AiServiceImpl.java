package com.aipm.ai_project_management.modules.ai.service.impl;

import com.aipm.ai_project_management.common.exceptions.ResourceNotFoundException;
import com.aipm.ai_project_management.modules.ai.dto.AiInsightDTO;
import com.aipm.ai_project_management.modules.ai.entity.AiInsightEntity;
import com.aipm.ai_project_management.modules.ai.repository.AiInsightRepository;
import com.aipm.ai_project_management.modules.ai.service.AiService;
import com.aipm.ai_project_management.modules.projects.repository.ProjectRepository;
import com.aipm.ai_project_management.modules.tasks.repository.TaskRepository;
import com.aipm.ai_project_management.modules.team.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AiServiceImpl implements AiService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);
    
    @Autowired
    private AiInsightRepository aiInsightRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Override
    public List<AiInsightDTO> generateProjectInsights(Long projectId) {
        logger.info("Generating AI insights for project ID: {}", projectId);
        
        // Validate project exists
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        
        List<AiInsightEntity> insights = new ArrayList<>();
        
        // Generate various types of insights
        insights.addAll(generateProjectRiskInsights(projectId));
        insights.addAll(generateProjectDeadlineInsights(projectId));
        insights.addAll(generateProjectBudgetInsights(projectId));
        insights.addAll(generateProjectResourceInsights(projectId));
        
        // Save all insights
        List<AiInsightEntity> savedInsights = aiInsightRepository.saveAll(insights);
        
        logger.info("Generated {} insights for project {}", savedInsights.size(), projectId);
        return savedInsights.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<AiInsightDTO> generateTaskInsights(Long taskId) {
        logger.info("Generating AI insights for task ID: {}", taskId);
        
        // Validate task exists
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        
        List<AiInsightEntity> insights = new ArrayList<>();
        
        // Generate task-specific insights
        insights.addAll(generateTaskPerformanceInsights(taskId));
        insights.addAll(generateTaskRecommendations(taskId));
        
        // Save all insights
        List<AiInsightEntity> savedInsights = aiInsightRepository.saveAll(insights);
        
        logger.info("Generated {} insights for task {}", savedInsights.size(), taskId);
        return savedInsights.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<AiInsightDTO> generateTeamInsights(Long teamId) {
        logger.info("Generating AI insights for team ID: {}", teamId);
        
        // Validate team exists
        if (!teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException("Team not found with id: " + teamId);
        }
        
        List<AiInsightEntity> insights = new ArrayList<>();
        
        // Generate team-specific insights
        insights.addAll(generateTeamProductivityInsights(teamId));
        insights.addAll(generateWorkloadBalanceInsights(teamId));
        insights.addAll(generateSkillGapInsights(teamId));
        
        // Save all insights
        List<AiInsightEntity> savedInsights = aiInsightRepository.saveAll(insights);
        
        logger.info("Generated {} insights for team {}", savedInsights.size(), teamId);
        return savedInsights.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AiInsightDTO> getActiveInsights(Pageable pageable) {
        logger.info("Fetching active AI insights");
        Page<AiInsightEntity> insights = aiInsightRepository.findByStatusOrderByCreatedAtDesc(
            AiInsightEntity.InsightStatus.ACTIVE, pageable);
        return insights.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AiInsightDTO> getInsightsByType(AiInsightEntity.InsightType type, Pageable pageable) {
        logger.info("Fetching AI insights by type: {}", type);
        Page<AiInsightEntity> insights = aiInsightRepository.findByTypeAndStatusOrderByCreatedAtDesc(
            type, AiInsightEntity.InsightStatus.ACTIVE, pageable);
        return insights.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getHighPriorityInsights() {
        logger.info("Fetching high priority AI insights");
        List<AiInsightEntity> insights = aiInsightRepository.findHighPriorityActiveInsights();
        return insights.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AiInsightDTO> getInsightsForEntity(String entityType, Long entityId) {
        logger.info("Fetching AI insights for entity: {} with ID: {}", entityType, entityId);
        List<AiInsightEntity> insights = aiInsightRepository.findByRelatedEntityTypeAndRelatedEntityIdAndStatus(
            entityType, entityId, AiInsightEntity.InsightStatus.ACTIVE);
        return insights.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public AiInsightDTO markInsightActionTaken(Long insightId, Long userId) {
        logger.info("Marking insight {} as action taken by user {}", insightId, userId);
        
        AiInsightEntity insight = aiInsightRepository.findById(insightId)
            .orElseThrow(() -> new ResourceNotFoundException("AI Insight not found with id: " + insightId));
        
        insight.setActionTaken(true);
        insight.setActionTakenAt(LocalDateTime.now());
        insight.setActionTakenBy(userId);
        insight.setStatus(AiInsightEntity.InsightStatus.IMPLEMENTED);
        
        AiInsightEntity savedInsight = aiInsightRepository.save(insight);
        return convertToDTO(savedInsight);
    }
    
    @Override
    public void dismissInsight(Long insightId) {
        logger.info("Dismissing insight with ID: {}", insightId);
        
        AiInsightEntity insight = aiInsightRepository.findById(insightId)
            .orElseThrow(() -> new ResourceNotFoundException("AI Insight not found with id: " + insightId));
        
        insight.setStatus(AiInsightEntity.InsightStatus.DISMISSED);
        aiInsightRepository.save(insight);
    }
    
    @Override
    public void cleanupExpiredInsights() {
        logger.info("Cleaning up expired AI insights");
        
        List<AiInsightEntity> expiredInsights = aiInsightRepository.findExpiredInsights(LocalDateTime.now());
        
        for (AiInsightEntity insight : expiredInsights) {
            insight.setStatus(AiInsightEntity.InsightStatus.EXPIRED);
        }
        
        aiInsightRepository.saveAll(expiredInsights);
        logger.info("Marked {} insights as expired", expiredInsights.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public AiInsightStats getInsightStatistics() {
        logger.info("Fetching AI insight statistics");
        
        long totalInsights = aiInsightRepository.count();
        long activeInsights = aiInsightRepository.countByStatus(AiInsightEntity.InsightStatus.ACTIVE);
        long dismissedInsights = aiInsightRepository.countByStatus(AiInsightEntity.InsightStatus.DISMISSED);
        long implementedInsights = aiInsightRepository.countByStatus(AiInsightEntity.InsightStatus.IMPLEMENTED);
        
        return new AiInsightStats(totalInsights, activeInsights, dismissedInsights, implementedInsights);
    }
    
    // Private helper methods for generating specific types of insights
    
    private List<AiInsightEntity> generateProjectRiskInsights(Long projectId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        // Simulate AI analysis - in real implementation, this would use ML models
        AiInsightEntity riskInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.PROJECT_RISK_ALERT,
            "Budget Risk Detected",
            "Project is 15% over budget and may exceed allocated funds by month end."
        );
        riskInsight.setPriority(AiInsightEntity.InsightPriority.HIGH);
        riskInsight.setRelatedEntityType("PROJECT");
        riskInsight.setRelatedEntityId(projectId);
        riskInsight.setConfidenceScore(0.85);
        riskInsight.setGeneratedBy("AI-Budget-Analyzer-v1.0");
        riskInsight.setExpiresAt(LocalDateTime.now().plusDays(7));
        
        insights.add(riskInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateProjectDeadlineInsights(Long projectId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity deadlineInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.DEADLINE_PREDICTION,
            "Deadline Risk Assessment",
            "Based on current velocity, project completion may be delayed by 3-5 days."
        );
        deadlineInsight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        deadlineInsight.setRelatedEntityType("PROJECT");
        deadlineInsight.setRelatedEntityId(projectId);
        deadlineInsight.setConfidenceScore(0.78);
        deadlineInsight.setGeneratedBy("AI-Deadline-Predictor-v1.0");
        
        insights.add(deadlineInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateProjectBudgetInsights(Long projectId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity budgetInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.BUDGET_FORECAST,
            "Budget Optimization Opportunity",
            "Reallocating 20% of design budget to development could improve delivery timeline."
        );
        budgetInsight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        budgetInsight.setRelatedEntityType("PROJECT");
        budgetInsight.setRelatedEntityId(projectId);
        budgetInsight.setConfidenceScore(0.72);
        budgetInsight.setGeneratedBy("AI-Budget-Optimizer-v1.0");
        
        insights.add(budgetInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateProjectResourceInsights(Long projectId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity resourceInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.RESOURCE_OPTIMIZATION,
            "Resource Allocation Suggestion",
            "Adding one senior developer could reduce project timeline by 15%."
        );
        resourceInsight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        resourceInsight.setRelatedEntityType("PROJECT");
        resourceInsight.setRelatedEntityId(projectId);
        resourceInsight.setConfidenceScore(0.68);
        resourceInsight.setGeneratedBy("AI-Resource-Optimizer-v1.0");
        
        insights.add(resourceInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateTaskPerformanceInsights(Long taskId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity performanceInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.PERFORMANCE_ANALYSIS,
            "Task Performance Analysis",
            "This task type typically takes 20% longer than estimated. Consider adjusting future estimates."
        );
        performanceInsight.setPriority(AiInsightEntity.InsightPriority.LOW);
        performanceInsight.setRelatedEntityType("TASK");
        performanceInsight.setRelatedEntityId(taskId);
        performanceInsight.setConfidenceScore(0.75);
        performanceInsight.setGeneratedBy("AI-Performance-Analyzer-v1.0");
        
        insights.add(performanceInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateTaskRecommendations(Long taskId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity recommendationInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.TASK_RECOMMENDATION,
            "Task Optimization Suggestion",
            "Breaking this task into 3 smaller subtasks could improve completion rate by 30%."
        );
        recommendationInsight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        recommendationInsight.setRelatedEntityType("TASK");
        recommendationInsight.setRelatedEntityId(taskId);
        recommendationInsight.setConfidenceScore(0.70);
        recommendationInsight.setGeneratedBy("AI-Task-Optimizer-v1.0");
        
        insights.add(recommendationInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateTeamProductivityInsights(Long teamId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity productivityInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.TEAM_PRODUCTIVITY,
            "Team Productivity Analysis",
            "Team productivity has increased 25% over the last sprint. Current velocity is optimal."
        );
        productivityInsight.setPriority(AiInsightEntity.InsightPriority.LOW);
        productivityInsight.setRelatedEntityType("TEAM");
        productivityInsight.setRelatedEntityId(teamId);
        productivityInsight.setConfidenceScore(0.82);
        productivityInsight.setGeneratedBy("AI-Productivity-Analyzer-v1.0");
        
        insights.add(productivityInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateWorkloadBalanceInsights(Long teamId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity workloadInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.WORKLOAD_BALANCE,
            "Workload Distribution Alert",
            "John Smith has 40% more tasks than team average. Consider redistributing workload."
        );
        workloadInsight.setPriority(AiInsightEntity.InsightPriority.HIGH);
        workloadInsight.setRelatedEntityType("TEAM");
        workloadInsight.setRelatedEntityId(teamId);
        workloadInsight.setConfidenceScore(0.88);
        workloadInsight.setGeneratedBy("AI-Workload-Analyzer-v1.0");
        
        insights.add(workloadInsight);
        return insights;
    }
    
    private List<AiInsightEntity> generateSkillGapInsights(Long teamId) {
        List<AiInsightEntity> insights = new ArrayList<>();
        
        AiInsightEntity skillGapInsight = new AiInsightEntity(
            AiInsightEntity.InsightType.SKILL_GAP_ANALYSIS,
            "Skill Gap Identified",
            "Team lacks expertise in React. Consider training or hiring a React specialist."
        );
        skillGapInsight.setPriority(AiInsightEntity.InsightPriority.MEDIUM);
        skillGapInsight.setRelatedEntityType("TEAM");
        skillGapInsight.setRelatedEntityId(teamId);
        skillGapInsight.setConfidenceScore(0.79);
        skillGapInsight.setGeneratedBy("AI-Skill-Analyzer-v1.0");
        
        insights.add(skillGapInsight);
        return insights;
    }
    
    private AiInsightDTO convertToDTO(AiInsightEntity entity) {
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