package com.aipm.ai_project_management.modules.team.dto;

public class SkillDTO {
    private String name;
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getYearsExperience() {
		return yearsExperience;
	}
	public void setYearsExperience(Integer yearsExperience) {
		this.yearsExperience = yearsExperience;
	}
	private String level;
    private Integer yearsExperience;
    // Getters and setters
}