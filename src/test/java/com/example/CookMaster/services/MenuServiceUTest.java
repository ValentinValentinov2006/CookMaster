package com.example.CookMaster.services;

import com.example.CookMaster.app.calendar.repository.MenuRepository;
import com.example.CookMaster.app.calendar.service.MenuService;
import com.example.CookMaster.app.dish.model.DishType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.CookMaster.app.calendar.model.DayOfWeek;
import com.example.CookMaster.app.calendar.model.Menu;
import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.MenuRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

class MenuServiceUTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private Dish breakfast;
    private Dish lunch;
    private Dish dinner;
    private User user;
    private MenuRequest menuRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        user = new User();
        user.setUsername("valkata");


        breakfast = new Dish(UUID.randomUUID(), "Pancakes", "Fluffy pancakes with syrup", DishType.BREAKFAST, LocalDate.now(), LocalDate.now(), user, new HashSet<>());

        lunch = new Dish(
                UUID.randomUUID(), "Salad", "Healthy mix of greens", DishType.LUNCH,
                LocalDate.now(), LocalDate.now(), user, new HashSet<>()
        );

        dinner = new Dish(
                UUID.randomUUID(), "Steak", "Grilled steak with garlic butter", DishType.DINNER,
                LocalDate.now(), LocalDate.now(), user, new HashSet<>()
        );

        menuRequest = new MenuRequest();
        menuRequest.setDayOfWeek(DayOfWeek.MONDAY.name());
    }

    @Test
    void testCreateMenu_ExistingMenu() {

        Menu existingMenu = new Menu();
        existingMenu.setDayOfWeek(DayOfWeek.MONDAY);
        when(menuRepository.findByDayOfWeek(DayOfWeek.MONDAY)).thenReturn(existingMenu);


        menuService.createMenu(breakfast, lunch, dinner, user, menuRequest);


        verify(menuRepository, times(1)).findByDayOfWeek(DayOfWeek.MONDAY);
        verify(menuRepository, times(1)).save(existingMenu);
    }

    @Test
    void testFindMenuByDay_ValidDay() {

        Menu menu = new Menu();
        menu.setDayOfWeek(DayOfWeek.MONDAY);
        when(menuRepository.findByDayOfWeek(DayOfWeek.MONDAY)).thenReturn(menu);

        Menu result = menuService.findMenuByDay(DayOfWeek.MONDAY.name());


        assertNotNull(result);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
        verify(menuRepository, times(1)).findByDayOfWeek(DayOfWeek.MONDAY);
    }

    @Test
    void testCreateMenu_NewMenu() {

        when(menuRepository.findByDayOfWeek(DayOfWeek.MONDAY)).thenReturn(null);

        menuService.createMenu(breakfast, lunch, dinner, user, menuRequest);


        verify(menuRepository, times(1)).findByDayOfWeek(DayOfWeek.MONDAY);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void testFindMenuByDay_MenuNotFound() {

        when(menuRepository.findByDayOfWeek(DayOfWeek.MONDAY)).thenReturn(null);

        Menu result = menuService.findMenuByDay(DayOfWeek.MONDAY.name());


        assertEquals(null, result);
        verify(menuRepository, times(1)).findByDayOfWeek(DayOfWeek.MONDAY);
    }
    @Test
    void testRemoveDishFromMenu() {



       Menu menu1 = new Menu();
       Menu menu2 = new Menu();


        menu1.setBreakfast(breakfast);
        menu2.setLunch(breakfast);


        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu1, menu2));

        menuService.removeDishFromMenu(breakfast);


        assertNull(menu1.getBreakfast(), "Breakfast dish should be removed from menu1");
        assertNull(menu2.getLunch(), "Lunch dish should be removed from menu2");


        verify(menuRepository, times(2)).save(any(Menu.class));
    }

    @Test
    void testRemoveDishFromMenuNoDishToRemove() {

        Menu menuWithoutDish = new Menu();
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menuWithoutDish));

        menuService.removeDishFromMenu(breakfast);


        verify(menuRepository, never()).save(any(Menu.class));
    }



}
