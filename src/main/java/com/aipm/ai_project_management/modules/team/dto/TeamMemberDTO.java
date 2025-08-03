package com.aipm.ai_project_management.modules.team.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class TeamMemberDTO {
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Status getStatus() {
	    return status;
	}
	public void setStatus(Status status) {
	    this.status = status;
	}

	public Availability getAvailability() {
	    return availability;
	}
	public void setAvailability(Availability availability) {
	    this.availability = availability;
	}
	public List<SkillDTO> getSkills() {
		return skills;
	}
	public void setSkills(List<SkillDTO> skills) {
		this.skills = skills;
	}
	public List<CertificationDTO> getCertifications() {
		return certifications;
	}
	public void setCertifications(List<CertificationDTO> certifications) {
		this.certifications = certifications;
	}
	public Integer getCurrentWorkload() {
		return currentWorkload;
	}
	public void setCurrentWorkload(Integer currentWorkload) {
		this.currentWorkload = currentWorkload;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public Double getHourlyRate() {
		return hourlyRate;
	}
	public void setHourlyRate(Double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}
	public Double getPerformanceScore() {
		return performanceScore;
	}
	public void setPerformanceScore(Double performanceScore) {
		this.performanceScore = performanceScore;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public LocalDate getHireDate() {
		return hireDate;
	}
	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ManagerDTO getManager() {
		return manager;
	}
	public void setManager(ManagerDTO manager) {
		this.manager = manager;
	}
	public List<ProjectSummaryDTO> getActiveProjects() {
		return activeProjects;
	}
	public void setActiveProjects(List<ProjectSummaryDTO> activeProjects) {
		this.activeProjects = activeProjects;
	}
	
	
	private Long teamId;

	public Long getTeamId() {
	    return teamId;
	}
	public void setTeamId(Long teamId) {
	    this.teamId = teamId;
	}
	
	private Long id;
    private Long userId;
    private String name;
    private String email;
    private String avatarUrl;
    private String role;
    private String department;
    public enum Status { active, inactive, pending }
    public enum Availability { available, busy, away }
    @Enumerated(EnumType.STRING)
    private Status status = Status.active;
    @Enumerated(EnumType.STRING)
    private Availability availability = Availability.available;
    private List<SkillDTO> skills;
    private List<CertificationDTO> certifications;
    private Integer currentWorkload;
    private Integer capacity;
    private Double hourlyRate;
    private Double performanceScore;
    private String location;
    private String timezone;
    private LocalDate hireDate;
    private String phone;
    private ManagerDTO manager;
    private List<ProjectSummaryDTO> activeProjects;
    // more fields as needed...

    // Getters and setters
}