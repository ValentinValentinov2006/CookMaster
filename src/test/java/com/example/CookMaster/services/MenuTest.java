package com.example.CookMaster.services;

import com.example.CookMaster.app.calendar.model.DayOfWeek;
import com.example.CookMaster.app.calendar.model.Menu;
import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class MenuTest {
    @Mock
    private User user;

    @Mock
    private Dish breakfastDish;

    @Mock
    private Dish lunchDish;

    @Mock
    private Dish dinnerDish;

    @InjectMocks
    private Menu menu;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menu = new Menu(UUID.randomUUID(), user, DayOfWeek.MONDAY, breakfastDish, lunchDish, dinnerDish, LocalDate.now());
    }

    @Test
    void testMenuCreation() {
        assertEquals(DayOfWeek.MONDAY, menu.getDayOfWeek());
        assertEquals(breakfastDish, menu.getBreakfast());
        assertEquals(lunchDish, menu.getLunch());
        assertEquals(dinnerDish, menu.getDinner());
        assertEquals(user, menu.getUser());
        assertEquals(LocalDate.now(), menu.getDate());
    }

    @Test
    void testDayOfWeekAssignment() {
        menu.setDayOfWeek(DayOfWeek.FRIDAY);
        assertEquals(DayOfWeek.FRIDAY, menu.getDayOfWeek());
    }

    @Test
    void testMenuWithDifferentDayOfWeek() {

        Menu fridayMenu = new Menu(UUID.randomUUID(), user, DayOfWeek.FRIDAY, breakfastDish, lunchDish, dinnerDish, LocalDate.now());

        assertEquals(DayOfWeek.FRIDAY, fridayMenu.getDayOfWeek());
    }
}
