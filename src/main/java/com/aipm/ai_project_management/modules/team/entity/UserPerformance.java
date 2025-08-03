package com.aipm.ai_project_management.modules.team.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.aipm.ai_project_management.modules.auth.entity.User;

@Entity
@Table(name = "user_performance")
public class UserPerformance {
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getOverallScore() {
		return overallScore;
	}

	public void setOverallScore(Double overallScore) {
		this.overallScore = overallScore;
	}

	public Integer getTasksCompleted() {
		return tasksCompleted;
	}

	public void setTasksCompleted(Integer tasksCompleted) {
		this.tasksCompleted = tasksCompleted;
	}

	public Integer getOnTimeDelivery() {
		return onTimeDelivery;
	}

	public void setOnTimeDelivery(Integer onTimeDelivery) {
		this.onTimeDelivery = onTimeDelivery;
	}

	public Double getCodeQuality() {
		return codeQuality;
	}

	public void setCodeQuality(Double codeQuality) {
		this.codeQuality = codeQuality;
	}

	public Double getTeamCollaboration() {
		return teamCollaboration;
	}

	public void setTeamCollaboration(Double teamCollaboration) {
		this.teamCollaboration = teamCollaboration;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="overall_score")
    private Double overallScore;

    @Column(name="tasks_completed")
    private Integer tasksCompleted;

    @Column(name="on_time_delivery")
    private Integer onTimeDelivery;

    @Column(name="code_quality")
    private Double codeQuality;

    @Column(name="team_collaboration")
    private Double teamCollaboration;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
}
