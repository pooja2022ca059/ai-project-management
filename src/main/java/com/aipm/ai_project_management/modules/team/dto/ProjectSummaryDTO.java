package com.aipm.ai_project_management.modules.team.dto;

import java.time.LocalDate;

public class ProjectSummaryDTO {
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getAllocation() {
		return allocation;
	}
	public void setAllocation(Integer allocation) {
		this.allocation = allocation;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	private Long id;
    private String name;
    private String role;
    private Integer allocation;
    private LocalDate startDate;
    private LocalDate endDate;
    // Getters and setters
}
