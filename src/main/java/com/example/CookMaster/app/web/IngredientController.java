package com.example.CookMaster.app.web;

import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

@Controller
@RequestMapping("ingredient")
public class IngredientController {
    private final IngredientService ingredientService;
    private final DishService dishService;

    @Autowired
    public IngredientController(IngredientService ingredientService, DishService dishService) {
        this.ingredientService = ingredientService;
        this.dishService = dishService;
    }

    @DeleteMapping("/remove")
    public String removeIngredient(@RequestParam UUID dishId,
                                   @RequestParam String dishName,
                                   @RequestParam String ingredient,
                                   RedirectAttributes redirectAttributes) {
        ingredientService.deleteIngredient(dishId, ingredient);

        redirectAttributes.addAttribute("id", dishId);
        return "redirect:/dish/searching?name=" + dishName;
        //http://localhost:8080/dish/searching?name=Tarator
    }



}




