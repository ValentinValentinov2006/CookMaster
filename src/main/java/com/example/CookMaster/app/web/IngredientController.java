package com.example.CookMaster.app.web;

import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("ingredient")
public class IngredientController {
    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping("/new")
    public String postIngredientRequest(@RequestParam("ingredientName") String ingredientName) {

        Ingredient ingredient = ingredientService.findIngredientByName(ingredientName);
        ingredientService.saveIngredient(ingredient);

        return "redirect:/edit-dish";
    }
}




