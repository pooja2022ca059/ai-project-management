package com.aipm.ai_project_management.modules.auth.security;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.aipm.ai_project_management.common.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private UserRole role;
    private User user; // Reference to entity

    // Constructor
    public UserPrincipal(Long id, String email, String name, String avatar, UserRole role, User user) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
        this.user = user;
    }

    // Static factory method
    public static UserPrincipal create(User user) {
        return new UserPrincipal(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getAvatar(),
            user.getRole(),
            user
        );
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getAvatar() { return avatar; }
    public UserRole getRole() { return role; }
    public User getUser() { return user; }

    // UserDetails methods (implement as needed)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }
    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}