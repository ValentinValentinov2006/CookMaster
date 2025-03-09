package com.example.CookMaster.app.ingredient.service;

import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class IngredientService {

    private final IngredientRepository mIngredientRepository;

    @Autowired
    public IngredientService(IngredientRepository mIngredientRepository) {
        this.mIngredientRepository = mIngredientRepository;
    }


    public void saveIngredient(Ingredient ingredient) {
        mIngredientRepository.save(ingredient);
    }

    public IngredientRepository getmIngredientRepository() {
        return mIngredientRepository;
    }


}
