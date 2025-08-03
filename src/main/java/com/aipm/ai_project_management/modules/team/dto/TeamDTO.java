package com.aipm.ai_project_management.modules.team.dto;

import java.util.List;

public class TeamDTO {
    private Long id;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public List<TeamMemberDTO> getMembers() {
		return members;
	}
	public void setMembers(List<TeamMemberDTO> members) {
		this.members = members;
	}
	private String name;
    private String description;
    private String department;
    private Long managerId;
    private String managerName;
    private List<TeamMemberDTO> members;

    // Getters and setters
}
