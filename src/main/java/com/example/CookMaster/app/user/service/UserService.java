package com.example.CookMaster.app.user.service;

import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.model.UserRole;
import com.example.CookMaster.app.user.repository.UserRepository;
import com.example.CookMaster.app.web.dto.LogInRequest;
import com.example.CookMaster.app.web.dto.ProfileEditRequest;
import com.example.CookMaster.app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private static final String INVALID_EMAIL_PASSWORD = "Email or password is incorrect.";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(RegisterRequest registerRequest) {

        Optional<User> isUser = userRepository.getUserByUsername(registerRequest.getUsername());
        if (isUser.isPresent()) {
            throw new DomainException(
                    "Username [%s] already exist."
                    .formatted(registerRequest.getUsername()));
        }

        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new DomainException("Passwords don't match.");
        }

        User user = userRepository.save(initializeUser(registerRequest));
        return user;
    }

   /* public User loginUser(LogInRequest loginRequest) {

        Optional<User> isUserExisted = userRepository.getUserByEmail(loginRequest.getEmail());
        if(isUserExisted.isEmpty()) {
            throw new DomainException(INVALID_EMAIL_PASSWORD);
        }

        User user = isUserExisted.get();
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new DomainException(INVALID_EMAIL_PASSWORD);
        }


        return user;
    }*/

    private User initializeUser(RegisterRequest registerRequest) {

        return User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .role(UserRole.USER)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(true)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));
        return new AuthenticationMetadata(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.getIsActive());
    }

    public User getById(UUID uuid) {
        return userRepository.getUserById(uuid);
    }

    public void editUserProfile(UUID id, @Valid ProfileEditRequest profileEditRequest) {

        User user = userRepository.getUserById(id);

        user.setUpdatedAt(LocalDate.now());

        if (profileEditRequest.getUrl() != null) {
            user.setUrl(profileEditRequest.getUrl());
        }
        if (profileEditRequest.getFirstName() != null) {
            user.setFirstName(profileEditRequest.getFirstName());
        }
        if (profileEditRequest.getLastName() != null) {
            user.setLastName(profileEditRequest.getLastName());
        }
        if (profileEditRequest.getEmail() != null) {
            user.setEmail(profileEditRequest.getEmail());
        }

        userRepository.save(user);
    }
}
