package com.aipm.ai_project_management.modules.auth.dto;

import com.aipm.ai_project_management.modules.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class LoginResponse {
    
    private String token;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    private UserDTO user;
    
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;
    
    // Default constructor
    public LoginResponse() {
    }
    
    // All-args constructor
    public LoginResponse(String token, String refreshToken, UserDTO user, LocalDateTime expiresAt) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
        this.expiresAt = expiresAt;
    }
    
    // Getters and setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResponse that = (LoginResponse) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(refreshToken, that.refreshToken) &&
                Objects.equals(user, that.user) &&
                Objects.equals(expiresAt, that.expiresAt);
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(token, refreshToken, user, expiresAt);
    }
    
    // toString method
    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + (token != null ? "[PRESENT]" : "[NOT PRESENT]") + '\'' +
                ", refreshToken='" + (refreshToken != null ? "[PRESENT]" : "[NOT PRESENT]") + '\'' +
                ", user=" + user +
                ", expiresAt=" + expiresAt +
                '}';
    }
    
    // Builder implementation
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String token;
        private String refreshToken;
        private UserDTO user;
        private LocalDateTime expiresAt;
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        
        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        
        public Builder user(UserDTO user) {
            this.user = user;
            return this;
        }
        
        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        
        public LoginResponse build() {
            return new LoginResponse(token, refreshToken, user, expiresAt);
        }
    }
    
    // UserDTO inner class
    public static class UserDTO {
        private Long id;
        private String email;
        private String name;
        private String role;
        private Set<String> permissions;
        private String avatar;
        
        // Default constructor
        public UserDTO() {
        }
        
        // All-args constructor
        public UserDTO(Long id, String email, String name, String role, Set<String> permissions, String avatar) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.role = role;
            this.permissions = permissions;
            this.avatar = avatar;
        }
        
        // Getters and setters
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
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public Set<String> getPermissions() {
            return permissions;
        }
        
        public void setPermissions(Set<String> permissions) {
            this.permissions = permissions;
        }
        
        public String getAvatar() {
            return avatar;
        }
        
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        
        // equals method
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserDTO userDTO = (UserDTO) o;
            return Objects.equals(id, userDTO.id) &&
                    Objects.equals(email, userDTO.email) &&
                    Objects.equals(name, userDTO.name) &&
                    Objects.equals(role, userDTO.role) &&
                    Objects.equals(permissions, userDTO.permissions) &&
                    Objects.equals(avatar, userDTO.avatar);
        }
        
        // hashCode method
        @Override
        public int hashCode() {
            return Objects.hash(id, email, name, role, permissions, avatar);
        }
        
        // toString method
        @Override
        public String toString() {
            return "UserDTO{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", name='" + name + '\'' +
                    ", role='" + role + '\'' +
                    ", permissions=" + permissions +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }
        
        // Static fromUser method - preserved from original
        public static UserDTO fromUser(User user) {
            return UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .permissions(user.getPermissions())
                    .avatar(user.getAvatar())
                    .build();
        }
        
        // Builder implementation for UserDTO
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private Long id;
            private String email;
            private String name;
            private String role;
            private Set<String> permissions;
            private String avatar;
            
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
            
            public Builder role(String role) {
                this.role = role;
                return this;
            }
            
            public Builder permissions(Set<String> permissions) {
                this.permissions = permissions;
                return this;
            }
            
            public Builder avatar(String avatar) {
                this.avatar = avatar;
                return this;
            }
            
            public UserDTO build() {
                return new UserDTO(id, email, name, role, permissions, avatar);
            }
        }
    }
}