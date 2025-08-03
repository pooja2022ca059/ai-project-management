package com.aipm.ai_project_management.modules.auth.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_sessions")
public class UserSession extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "refresh_token", unique = true, nullable = false)
    private String refreshToken;
    
    @Column(name = "device_info")
    private String deviceInfo;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;
    
    @Column(name = "revoked_reason")
    private String revokedReason;
    
    // Default constructor (replaces @NoArgsConstructor)
    public UserSession() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public UserSession(Long id, User user, String refreshToken, String deviceInfo, 
                       String ipAddress, String userAgent, LocalDateTime expiresAt, 
                       boolean active, LocalDateTime revokedAt, String revokedReason) {
        this.id = id;
        this.user = user;
        this.refreshToken = refreshToken;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.expiresAt = expiresAt;
        this.active = active;
        this.revokedAt = revokedAt;
        this.revokedReason = revokedReason;
    }
    
    // Getters and setters (part of @Data replacement)
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
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getDeviceInfo() {
        return deviceInfo;
    }
    
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }
    
    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }
    
    public String getRevokedReason() {
        return revokedReason;
    }
    
    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
    }
    
    // Business logic methods (kept as-is)
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
    
    public boolean isValid() {
        return active && !isExpired() && revokedAt == null;
    }
    
    public void revoke(String reason) {
        this.active = false;
        this.revokedAt = LocalDateTime.now();
        this.revokedReason = reason;
    }
    
    // equals method (replaces @EqualsAndHashCode with callSuper = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        
        UserSession that = (UserSession) o;
        
        if (active != that.active) return false;
        if (!Objects.equals(id, that.id)) return false;
        // For lazy-loaded entities, compare by ID
        if (user != null && that.user != null) {
            if (!Objects.equals(user.getId(), that.user.getId())) return false;
        } else if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(refreshToken, that.refreshToken)) return false;
        if (!Objects.equals(deviceInfo, that.deviceInfo)) return false;
        if (!Objects.equals(ipAddress, that.ipAddress)) return false;
        if (!Objects.equals(userAgent, that.userAgent)) return false;
        if (!Objects.equals(expiresAt, that.expiresAt)) return false;
        if (!Objects.equals(revokedAt, that.revokedAt)) return false;
        return Objects.equals(revokedReason, that.revokedReason);
    }
    
    // hashCode method (replaces @EqualsAndHashCode with callSuper = true)
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (user != null ? user.getId().hashCode() : 0); // Use ID for lazy-loaded entity
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        result = 31 * result + (deviceInfo != null ? deviceInfo.hashCode() : 0);
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (expiresAt != null ? expiresAt.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (revokedAt != null ? revokedAt.hashCode() : 0);
        result = 31 * result + (revokedReason != null ? revokedReason.hashCode() : 0);
        return result;
    }
    
    // toString method (part of @Data replacement)
    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", refreshToken='" + (refreshToken != null ? "[PROTECTED]" : null) + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", expiresAt=" + expiresAt +
                ", active=" + active +
                ", revokedAt=" + revokedAt +
                ", revokedReason='" + revokedReason + '\'' +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private User user;
        private String refreshToken;
        private String deviceInfo;
        private String ipAddress;
        private String userAgent;
        private LocalDateTime expiresAt;
        private boolean active = true;
        private LocalDateTime revokedAt;
        private String revokedReason;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder user(User user) {
            this.user = user;
            return this;
        }
        
        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        
        public Builder deviceInfo(String deviceInfo) {
            this.deviceInfo = deviceInfo;
            return this;
        }
        
        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }
        
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        
        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        
        public Builder active(boolean active) {
            this.active = active;
            return this;
        }
        
        public Builder revokedAt(LocalDateTime revokedAt) {
            this.revokedAt = revokedAt;
            return this;
        }
        
        public Builder revokedReason(String revokedReason) {
            this.revokedReason = revokedReason;
            return this;
        }
        
        public UserSession build() {
            return new UserSession(id, user, refreshToken, deviceInfo, ipAddress,
                    userAgent, expiresAt, active, revokedAt, revokedReason);
        }
    }
}