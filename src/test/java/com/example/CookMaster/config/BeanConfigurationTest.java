package com.example.CookMaster.config;

import com.example.CookMaster.app.config.BeanConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class BeanConfigurationTest {

    @Test
    void testPasswordEncoderBean_ShouldReturnBCryptPasswordEncoder() {

        BeanConfiguration config = new BeanConfiguration();


        PasswordEncoder encoder = config.passwordEncoder();


        assertNotNull(encoder, "PasswordEncoder bean should not be null");
        assertTrue(encoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder,
                "PasswordEncoder should be an instance of BCryptPasswordEncoder");


        String rawPassword = "test123";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword, "Encoded password should not match raw password");
        assertTrue(encoder.matches(rawPassword, encodedPassword), "Password should match after encoding");
    }
}
