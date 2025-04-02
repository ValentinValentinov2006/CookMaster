package com.example.CookMaster.dto;

import com.example.CookMaster.app.calendar.model.DayOfWeek;
import com.example.CookMaster.app.calendar.model.Menu;
import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.EditDishRequest;
import com.example.CookMaster.app.web.dto.MenuRequest;
import com.example.CookMaster.app.web.dto.ProfileEditRequest;
import com.example.CookMaster.app.web.mapper.DtoMapper;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoMapperTest {

    @Test
    public void testMapUserToUserEditRequest() {

        User user = new User();
        user.setFirstName("Valentin");
        user.setLastName("Valentinov");
        user.setEmail("valanval@gmail.com");
        user.setUrl("www.vv.com");


        ProfileEditRequest result = DtoMapper.mapUserToUserEditRequest(user);

        assertEquals("Valentin", result.getFirstName());
        assertEquals("Valentinov", result.getLastName());
        assertEquals("valanval@gmail.com", result.getEmail());
        assertEquals("www.vv.com", result.getUrl());
    }


    @Test
    public void testMapDishToEditDishRequest() {

        Dish dish = new Dish();
        dish.setName("Pasta");
        dish.setType(DishType.BREAKFAST);
        dish.setDescription("A delicious Italian pasta");

        Ingredient ingredient1 = Ingredient.builder()
                .name("Garlic")
                .isBought(true)
                .build();

        Ingredient ingredient2 = Ingredient
                .builder()
                .name("Tomato")
                .isBought(false)
                .build();

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        dish.setIngredients(ingredients);

        EditDishRequest result = DtoMapper.mapDishToEditDishRequest(dish);

        assertEquals("Pasta", result.getDishName());
        assertEquals(DishType.BREAKFAST, result.getDishType());
        assertEquals("A delicious Italian pasta", result.getDishDescription());
        assertEquals(2, result.getIngredients().size());
        assertEquals(true, result.getIngredients().contains("Tomato"));
        assertEquals(true, result.getIngredients().contains("Garlic"));
    }


    @Test
    public void testMapMenuRequestToMenu() {

        MenuRequest menuRequest = new MenuRequest();
        menuRequest.setDayOfWeek("Monday");
        Menu result = DtoMapper.mapMenuRequestToMenu(menuRequest);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    }
}
