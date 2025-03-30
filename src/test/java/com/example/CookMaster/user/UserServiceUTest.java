package com.example.CookMaster.user;


import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.model.UserRole;
import com.example.CookMaster.app.user.repository.UserRepository;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.ProfileEditRequest;
import com.example.CookMaster.app.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_UserExists_ShouldThrowDomainException() {

        RegisterRequest registerRequest = new RegisterRequest("existingUser", "password", "password", "test@example.com");


        when(userRepository.getUserByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new User()));


        DomainException exception = assertThrows(DomainException.class, () -> {
            userService.registerUser(registerRequest);
        });

        assertEquals("Username [existingUser] already exist.", exception.getMessage());
    }

    @Test
    void testRegisterUser_PasswordMismatch_ShouldThrowDomainException() {

        RegisterRequest registerRequest = new RegisterRequest("newUser", "password", "mismatchPassword", "test@example.com");


        DomainException exception = assertThrows(DomainException.class, () -> {
            userService.registerUser(registerRequest);
        });

        assertEquals("Passwords don't match.", exception.getMessage());
    }

    @Test
    void testRegisterUser_Success() {

        RegisterRequest registerRequest = new RegisterRequest("newUser", "test@example.com", "password", "password");


        when(userRepository.getUserByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");


        userService.registerUser(registerRequest);


        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testGetById_UserFound_ShouldReturnUser() {

        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepository.getUserById(userId)).thenReturn(user);

        User foundUser = userService.getById(userId);

        assertEquals(userId, foundUser.getId());
    }

    @Test
    void testEditUserProfile_Success() {

        UUID userId = UUID.randomUUID();
        ProfileEditRequest profileEditRequest = ProfileEditRequest.builder()
                .firstName("Misho")
                .url("https://static.vecteezy.com/system/resources/thumbnails/036/324/708/small/ai-generated-picture-of-a-tiger-walking-in-the-forest-photo.jpg")
                .lastName("Mihov")
                .email("misho@gmail.com")
                .build();

        User user = new User();
        user.setId(userId);

        when(userRepository.getUserById(userId)).thenReturn(user);

        userService.editUserProfile(userId, profileEditRequest);


        assertEquals("Misho", user.getFirstName());
        assertEquals("Mihov", user.getLastName());
        assertEquals("https://static.vecteezy.com/system/resources/thumbnails/036/324/708/small/ai-generated-picture-of-a-tiger-walking-in-the-forest-photo.jpg", user.getUrl());
        assertEquals("misho@gmail.com", user.getEmail());

        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testChangeStatus_AdminToUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setRole(UserRole.ADMIN);

        when(userRepository.getUserById(userId)).thenReturn(user);
        userService.changeStatus(userId);
        assertEquals(UserRole.USER, user.getRole());
    }

    @Test
    void testChangeStatus_UserToAdmin() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setRole(UserRole.USER);

        when(userRepository.getUserById(userId)).thenReturn(user);
        userService.changeStatus(userId);
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testGetAllUsers_ShouldReturnListOfUsers() {

        List<User> users = new ArrayList<>();
        User userFirst = new User();
        User userSecond = new User();
        users.add(userFirst);
        users.add(userSecond);

        when(userRepository.findAll()).thenReturn(users);
        List<User> allUsers = userService.getAllUsers();
        assertEquals(2, allUsers.size());
    }
}
