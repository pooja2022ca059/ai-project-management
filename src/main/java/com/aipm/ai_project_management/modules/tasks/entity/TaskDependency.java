package com.aipm.ai_project_management.modules.tasks.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;


import jakarta.persistence.*;


@Entity
@Table(name = "task_dependencies", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"dependent_task_id", "dependency_task_id"}))
public class TaskDependency extends AuditableEntity {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Task getDependentTask() {
		return dependentTask;
	}

	public void setDependentTask(Task dependentTask) {
		this.dependentTask = dependentTask;
	}

	public Task getDependencyTask() {
		return dependencyTask;
	}

	public void setDependencyTask(Task dependencyTask) {
		this.dependencyTask = dependencyTask;
	}

	public String getDependencyType() {
		return dependencyType;
	}

	public void setDependencyType(String dependencyType) {
		this.dependencyType = dependencyType;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_task_id", nullable = false)
    private Task dependentTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependency_task_id", nullable = false)
    private Task dependencyTask;
    
    @Column(name = "dependency_type")
    private String dependencyType; // FINISH_TO_START, START_TO_START, etc.
}
