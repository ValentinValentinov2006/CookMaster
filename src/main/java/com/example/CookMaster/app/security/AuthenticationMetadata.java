package com.example.CookMaster.app.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import com.example.CookMaster.app.user.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.UUID;
import java.util.Collection;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
public class AuthenticationMetadata implements UserDetails {

    private UUID userId;
    private String username;
    private String password;
    private UserRole role;
    private boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // hasRole("ADMIN") -> "ROLE_ADMIN"
        // hasAuthority("ADMIN") -> "ADMIN"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        System.out.println("User Role: " + role.name());
        System.out.println("Granted Authority: " + authority.getAuthority());
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
