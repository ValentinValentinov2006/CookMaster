package com.example.CookMaster.meta;

import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.UserRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationMetadataTest {

    @Test
    void testIsAccountNonExpired_ShouldReturnTrue() {
        AuthenticationMetadata user = new AuthenticationMetadata(
                UUID.randomUUID(), "testUser", "password", UserRole.USER, true);

        assertTrue(user.isAccountNonExpired(), "Account should not be expired");
    }

    @Test
    void testIsAccountNonLocked_ShouldReturnTrue() {
        AuthenticationMetadata user = new AuthenticationMetadata(
                UUID.randomUUID(), "testUser", "password", UserRole.USER, true);

        assertTrue(user.isAccountNonLocked(), "Account should not be locked");
    }

    @Test
    void testIsCredentialsNonExpired_ShouldReturnTrue_WhenActive() {
        AuthenticationMetadata user = new AuthenticationMetadata(
                UUID.randomUUID(), "testUser", "password", UserRole.USER, true);

        assertTrue(user.isCredentialsNonExpired(), "Credentials should not be expired when active");
    }

    @Test
    void testIsCredentialsNonExpired_ShouldReturnFalse_WhenInactive() {
        AuthenticationMetadata user = new AuthenticationMetadata(
                UUID.randomUUID(), "testUser", "password", UserRole.USER, false);

        assertFalse(user.isCredentialsNonExpired(), "Credentials should be expired when inactive");
    }

    @Test
    void testIsEnabled_ShouldReturnTrue_WhenActive() {
        AuthenticationMetadata user = new AuthenticationMetadata(
                UUID.randomUUID(), "testUser", "password", UserRole.USER, true);

        assertTrue(user.isEnabled(), "User should be enabled when active");
    }

    @Test
    void testIsEnabled_ShouldReturnFalse_WhenInactive() {
        AuthenticationMetadata user = new AuthenticationMetadata(
                UUID.randomUUID(), "testUser", "password", UserRole.USER, false);

        assertFalse(user.isEnabled(), "User should be disabled when inactive");
    }
}
