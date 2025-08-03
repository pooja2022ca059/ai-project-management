package com.aipm.ai_project_management.modules.auth.entity;

import com.aipm.ai_project_management.shared.audit.AuditableEntity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    @Column(name = "is_used")
    private boolean used = false;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    // Default constructor (replaces @NoArgsConstructor)
    public PasswordResetToken() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public PasswordResetToken(Long id, String token, User user, LocalDateTime expiresAt,
                              LocalDateTime usedAt, boolean used, String ipAddress, String userAgent) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
        this.used = used;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
    
    // Getters and setters (part of @Data replacement)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public LocalDateTime getUsedAt() {
        return usedAt;
    }
    
    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
    
    public boolean isUsed() {
        return used;
    }
    
    public void setUsed(boolean used) {
        this.used = used;
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
    
    // Business logic methods (kept as-is)
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return !used && !isExpired();
    }
    
    public void markAsUsed() {
        this.used = true;
        this.usedAt = LocalDateTime.now();
    }
    
    // equals method (replaces @EqualsAndHashCode with callSuper = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        
        PasswordResetToken that = (PasswordResetToken) o;
        
        if (used != that.used) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(token, that.token)) return false;
        // For lazy-loaded entities, compare IDs rather than objects
        if (user != null && that.user != null) {
            if (!Objects.equals(user.getId(), that.user.getId())) return false;
        } else if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(expiresAt, that.expiresAt)) return false;
        if (!Objects.equals(usedAt, that.usedAt)) return false;
        if (!Objects.equals(ipAddress, that.ipAddress)) return false;
        return Objects.equals(userAgent, that.userAgent);
    }
    
    // hashCode method (replaces @EqualsAndHashCode with callSuper = true)
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (user != null ? user.getId().hashCode() : 0); // Use ID for lazy-loaded entity
        result = 31 * result + (expiresAt != null ? expiresAt.hashCode() : 0);
        result = 31 * result + (usedAt != null ? usedAt.hashCode() : 0);
        result = 31 * result + (used ? 1 : 0);
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        return result;
    }
    
    // toString method (part of @Data replacement) - with security considerations
    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "id=" + id +
                ", token='" + (token != null ? "[PROTECTED]" : null) + '\'' +
                ", user=" + (user != null ? user.getId() : null) +
                ", expiresAt=" + expiresAt +
                ", usedAt=" + usedAt +
                ", used=" + used +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
    
    // Builder implementation (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private String token;
        private User user;
        private LocalDateTime expiresAt;
        private LocalDateTime usedAt;
        private boolean used = false;
        private String ipAddress;
        private String userAgent;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        
        public Builder user(User user) {
            this.user = user;
            return this;
        }
        
        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        
        public Builder usedAt(LocalDateTime usedAt) {
            this.usedAt = usedAt;
            return this;
        }
        
        public Builder used(boolean used) {
            this.used = used;
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
        
        public PasswordResetToken build() {
            return new PasswordResetToken(id, token, user, expiresAt, usedAt, used, ipAddress, userAgent);
        }
    }
}