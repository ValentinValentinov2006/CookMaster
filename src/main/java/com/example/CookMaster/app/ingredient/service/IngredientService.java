package com.example.CookMaster.app.ingredient.service;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.exception.DomainException;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.repository.IngredientRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
@Slf4j
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final DishService dishService;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, @Lazy DishService dishService) {
        this.ingredientRepository = ingredientRepository;
        this.dishService = dishService;
    }


    public IngredientRepository getmIngredientRepository() {
        return ingredientRepository;
    }



    public void deleteIngredient(UUID dishId, String ingredient) {
        Dish dish = dishService.findDishNyId(dishId);

        dish.getIngredients().remove(ingredient);
        log.info("Deleting ingredient: " + ingredient + " from dish: " + dish);
        dishService.getMDishRepository().save(dish);
        log.info("Successfully save dish after deletion: " + ingredient + " from dish: " + dish);
    }
}
