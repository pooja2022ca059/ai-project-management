package com.aipm.ai_project_management.modules.team.dto;

import java.time.LocalDate;

public class CertificationDTO {
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalDate getExpiry() {
		return expiry;
	}
	public void setExpiry(LocalDate expiry) {
		this.expiry = expiry;
	}
	private String name;
    private String issuer;
    private LocalDate date;
    private LocalDate expiry;
    // Getters and setters
}