package com.aipm.ai_project_management.modules.auth.entity;

import com.aipm.ai_project_management.common.enums.UserRole;
import com.aipm.ai_project_management.shared.audit.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User extends AuditableEntity implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String name;
    
    @JsonIgnore
    @Column(name = "password_hash", nullable = false)  // ✅ FIXED: Added column mapping
    private String password;
    
    @Column(name = "avatar_url")
    private String avatar;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    // ✅ ADDED: Missing database fields
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "timezone")
    private String timezone;
    
    @Column(name = "language")
    private String language;
    
    @Column(name = "email_verified")
    private Boolean emailVerified;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @Column(name = "is_email_verified")
    private boolean isEmailVerified = false;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "login_attempts")
    private Integer loginAttempts;
    
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<String> permissions = new HashSet<>();
    
    @Column(name = "two_factor_enabled")
    private boolean twoFactorEnabled = false;
    
    @Column(name = "two_factor_secret")
    @JsonIgnore
    private String twoFactorSecret;
    
    // ✅ ADDED: UserStatus enum
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
    
    // Default constructor (replaces @NoArgsConstructor)
    public User() {
        this.permissions = new HashSet<>();
        // ✅ ADDED: Default values
        this.active = true;
        this.isEmailVerified = false;
        this.emailVerified = false;
        this.loginAttempts = 0;
        this.twoFactorEnabled = false;
        this.status = UserStatus.ACTIVE;
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public User(Long id, String email, String name, String password, String avatar,
                UserRole role, boolean active, boolean emailVerified, LocalDateTime lastLogin,
                int loginAttempts, LocalDateTime lockedUntil, Set<String> permissions,
                boolean twoFactorEnabled, String twoFactorSecret) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.role = role;
        this.active = active;
        this.emailVerified = emailVerified;
        this.isEmailVerified = emailVerified;
        this.lastLogin = lastLogin;
        this.loginAttempts = loginAttempts;
        this.lockedUntil = lockedUntil;
        this.permissions = permissions != null ? permissions : new HashSet<>();
        this.twoFactorEnabled = twoFactorEnabled;
        this.twoFactorSecret = twoFactorSecret;
        this.status = UserStatus.ACTIVE;
    }
    
    // ✅ ADDED: Getters and Setters for new fields
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
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
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public LocalDateTime getPasswordChangedAt() {
        return passwordChangedAt;
    }
    
    public void setPasswordChangedAt(LocalDateTime passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        this.isEmailVerified = emailVerified != null ? emailVerified : false;
    }
    
    public boolean getIsEmailVerified() {
        return isEmailVerified;
    }
    
    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
        this.emailVerified = isEmailVerified;
    }
    
    // Existing getters and setters (keeping as-is)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isEmailVerified() {
        return isEmailVerified;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public int getLoginAttempts() {
        return loginAttempts;
    }
    
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    
    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }
    
    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions != null ? permissions : new HashSet<>();
    }
    
    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }
    
    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }
    
    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }
    
    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }
    
    // UserDetails implementation methods (kept as-is)
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        authorities.addAll(permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()));
        return authorities;
    }
    
    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return lockedUntil == null || lockedUntil.isBefore(LocalDateTime.now());
    }
    
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return active;
    }
    
    // Business logic methods (kept as-is)
    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= 5) {
            this.lockedUntil = LocalDateTime.now().plusMinutes(30);
        }
    }
    
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.lockedUntil = null;
    }
    
    // equals method (keeping existing implementation)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        
        User user = (User) o;
        
        if (active != user.active) return false;
        if (isEmailVerified != user.isEmailVerified) return false;
        if (loginAttempts != user.loginAttempts) return false;
        if (twoFactorEnabled != user.twoFactorEnabled) return false;
        if (!Objects.equals(id, user.id)) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(name, user.name)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(avatar, user.avatar)) return false;
        if (role != user.role) return false;
        if (!Objects.equals(lastLogin, user.lastLogin)) return false;
        if (!Objects.equals(lockedUntil, user.lockedUntil)) return false;
        if (!Objects.equals(permissions, user.permissions)) return false;
        return Objects.equals(twoFactorSecret, user.twoFactorSecret);
    }
    
    // hashCode method (keeping existing implementation)
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (isEmailVerified ? 1 : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + loginAttempts;
        result = 31 * result + (lockedUntil != null ? lockedUntil.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        result = 31 * result + (twoFactorEnabled ? 1 : 0);
        result = 31 * result + (twoFactorSecret != null ? twoFactorSecret.hashCode() : 0);
        return result;
    }
    
    // toString method (keeping existing implementation with security considerations)
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='[PROTECTED]'" +
                ", avatar='" + avatar + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", location='" + location + '\'' +
                ", timezone='" + timezone + '\'' +
                ", language='" + language + '\'' +
                ", active=" + active +
                ", isEmailVerified=" + isEmailVerified +
                ", lastLogin=" + lastLogin +
                ", loginAttempts=" + loginAttempts +
                ", lockedUntil=" + lockedUntil +
                ", permissions=" + permissions +
                ", twoFactorEnabled=" + twoFactorEnabled +
                ", twoFactorSecret='[PROTECTED]'" +
                '}';
    }
    
    // Builder implementation (keeping existing with minor updates)
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private String email;
        private String name;
        private String password;
        private String avatar;
        private UserRole role;
        private UserStatus status = UserStatus.ACTIVE;
        private String phone;
        private String location;
        private String timezone;
        private String language;
        private boolean active = true;
        private boolean isEmailVerified = false;
        private Boolean emailVerified = false;
        private LocalDateTime lastLogin;
        private int loginAttempts = 0;
        private LocalDateTime lockedUntil;
        private Set<String> permissions = new HashSet<>();
        private boolean twoFactorEnabled = false;
        private String twoFactorSecret;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }
        
        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }
        
        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        
        public Builder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }
        
        public Builder language(String language) {
            this.language = language;
            return this;
        }
        
        public Builder active(boolean active) {
            this.active = active;
            return this;
        }
        
        public Builder isEmailVerified(boolean isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
            this.emailVerified = isEmailVerified;
            return this;
        }
        
        public Builder emailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
            this.isEmailVerified = emailVerified != null ? emailVerified : false;
            return this;
        }
        
        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }
        
        public Builder loginAttempts(int loginAttempts) {
            this.loginAttempts = loginAttempts;
            return this;
        }
        
        public Builder lockedUntil(LocalDateTime lockedUntil) {
            this.lockedUntil = lockedUntil;
            return this;
        }
        
        public Builder permissions(Set<String> permissions) {
            this.permissions = permissions != null ? permissions : new HashSet<>();
            return this;
        }
        
        public Builder twoFactorEnabled(boolean twoFactorEnabled) {
            this.twoFactorEnabled = twoFactorEnabled;
            return this;
        }
        
        public Builder twoFactorSecret(String twoFactorSecret) {
            this.twoFactorSecret = twoFactorSecret;
            return this;
        }
        
        public User build() {
            User user = new User(id, email, name, password, avatar, role, active,
                    emailVerified, lastLogin, loginAttempts, lockedUntil,
                    permissions, twoFactorEnabled, twoFactorSecret);
            user.setStatus(status);
            user.setPhone(phone);
            user.setLocation(location);
            user.setTimezone(timezone);
            user.setLanguage(language);
            return user;
        }
    }

	public String getDepartment() {
		// TODO Auto-generated method stub
		return null;
	}
}