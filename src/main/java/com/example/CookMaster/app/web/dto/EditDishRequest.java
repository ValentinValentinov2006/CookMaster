package com.example.CookMaster.app.web.dto;

import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class EditDishRequest {

    @Size(min = 1, message = "Dish's Name cannot be empty")
    private String dishName;


    private DishType dishType;


    @Size(min = 5, message = "Dish's Description should be at least 5 characters")
    private String dishDescription;


    private Set<Ingredient> ingredients;
}
