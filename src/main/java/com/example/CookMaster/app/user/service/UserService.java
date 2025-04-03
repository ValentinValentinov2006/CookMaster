package com.example.CookMaster.app.user.service;


import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.model.UserRole;
import com.example.CookMaster.app.user.repository.UserRepository;
import com.example.CookMaster.app.web.dto.ProfileEditRequest;
import com.example.CookMaster.app.web.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {

        Optional<User> isUser = userRepository.getUserByUsername(registerRequest.getUsername());
        if (isUser.isPresent()) {
            throw new DomainException(
                    "Username [%s] already exist."
                    .formatted(registerRequest.getUsername()));
        }

        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new DomainException("Passwords don't match.");
        }

      userRepository.save(initializeUser(registerRequest));
    }



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
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new DomainException("User not found."));
        return new AuthenticationMetadata(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.getIsActive());
    }

    public User getById(UUID uuid) {
        return userRepository.getUserById(uuid);
    }

    public void editUserProfile(UUID id,  ProfileEditRequest profileEditRequest) {

        User user = userRepository.getUserById(id);

        user.setUpdatedAt(LocalDate.now());

        if (profileEditRequest.getUrl() == null || profileEditRequest.getFirstName() == null ||
         profileEditRequest.getLastName() == null || profileEditRequest.getEmail() == null) {
            System.out.println("!!! THROWING EditProfileException !!!");
           throw new DomainException("Invalid profile edit.");
        }
        user.setUrl(profileEditRequest.getUrl());
        user.setLastName(profileEditRequest.getLastName());
        user.setFirstName(profileEditRequest.getFirstName());
        user.setEmail(profileEditRequest.getEmail());


        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public void changeStatus(UUID id) {
        User user = userRepository.getUserById(id);
        user.setRole(user.getRole() == UserRole.ADMIN ? UserRole.USER : UserRole.ADMIN);
        userRepository.save(user);
        log.info("Successfully changed status of user [{}]", user.getUsername());
    }


}
