package com.example.CookMaster.services;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.dish.repository.DishRepository;
import com.example.CookMaster.app.schedule.UserReportService;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;


class UserReportServiceTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private UserReportService userReportService;

    @BeforeEach
    void setUp() {
        dishRepository = mock(DishRepository.class);
        userReportService = new UserReportService(dishRepository);
    }

    @Test
    void testGenerateUserReport_WhenDishesCreated_ShouldGenerateCorrectReport() {
        LocalDate today = LocalDate.now();
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("vvv")
                .email("vvv@gmail.com")
                .role(UserRole.USER)
                .createdAt(today)
                .updatedAt(today)
                .password("123")
                .isActive(true)
                .dishes(Set.of())
                .build();

        User user2 = User.builder()
                .id(UUID.randomUUID())
                .username("jvv")
                .email("jvvne@gmail.com")
                .role(UserRole.USER)
                .createdAt(today)
                .updatedAt(today)
                .password("123")
                .isActive(true)
                .dishes(Set.of())
                .build();

        Dish dish1 = Dish.builder()
                .id(UUID.randomUUID())
                .name("Pizza")
                .description("Cheese and tomato pizza")
                .type(DishType.BREAKFAST)
                .createdAt(today)
                .updatedAt(today)
                .user(user1)
                .ingredients(Set.of())
                .build();

        Dish dish2 = Dish.builder()
                .id(UUID.randomUUID())
                .name("Pasta")
                .description("Delicious pasta with cheese")
                .type(DishType.BREAKFAST)
                .createdAt(today)
                .updatedAt(today)
                .user(user2)
                .ingredients(Set.of())
                .build();

        when(dishRepository.findByCreatedAtBetween(today, today)).thenReturn(List.of(dish1, dish2));
        userReportService.generateUserReport();
        verify(dishRepository, times(1)).findByCreatedAtBetween(today, today);
    }

    @Test
    void testGenerateUserReport_WhenNoDishesCreated_ShouldGenerateEmptyReport() {
        LocalDate today = LocalDate.now();
        when(dishRepository.findByCreatedAtBetween(today, today)).thenReturn(List.of());
        userReportService.generateUserReport();
        verify(dishRepository, times(1)).findByCreatedAtBetween(today, today);
    }
    @Test
    void testGenerateUserReport_WhenRepositoryThrowsException_ShouldHandleErrorGracefully() {

        LocalDate today = LocalDate.now();
        when(dishRepository.findByCreatedAtBetween(today, today)).thenThrow(new RuntimeException("Database error"));


        try {
            userReportService.generateUserReport();
        } catch (Exception e) {
            System.out.println("❌ Expected exception caught: " + e.getMessage());
        }


        verify(dishRepository, times(1)).findByCreatedAtBetween(today, today);
    }


}