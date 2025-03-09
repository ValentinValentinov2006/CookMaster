package com.example.CookMaster.app.web.dto;

import com.example.CookMaster.app.dish.model.DishType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Data
public class CreateDishRequest {


    @NotNull(message = "Dish's Name is required!")
    @Size(min = 1, message = "Dish's Name cannot be empty")
    private String dishName;

    @NotNull(message = "Dish's Type is required!")
    private DishType dishType;

    @NotNull(message = "Dish's Description is required!")
    @Size(min = 5, message = "Dish's Description should be at least 5 characters")
    private String dishDescription;

    @NotNull(message = "Ingredients are required!")
    private Set<String> ingredients;
}
