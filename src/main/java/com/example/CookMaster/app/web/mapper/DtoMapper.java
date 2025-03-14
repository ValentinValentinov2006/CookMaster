package com.example.CookMaster.app.web.mapper;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import com.example.CookMaster.app.web.dto.EditDishRequest;
import com.example.CookMaster.app.web.dto.ProfileEditRequest;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class DtoMapper {

    public static ProfileEditRequest mapUserToUserEditRequest(User user) {

        return ProfileEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .url(user.getUrl())
                .build();
    }

    public static EditDishRequest mapDishToEditDishRequest(Dish dish) {
        return EditDishRequest.builder()
                .dishName(dish.getName())
                .dishType(dish.getType())
                .dishDescription(dish.getDescription())
                .ingredients(convertIngredientsToNames(dish.getIngredients()))
                .build();
    }
    private Set<String> convertIngredientsToNames(Set<Ingredient> ingredients) {
        return ingredients.stream()
                .map(Ingredient::getName)
                .collect(Collectors.toSet());
    }
}
