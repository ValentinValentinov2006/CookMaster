package com.example.CookMaster.app.web;


import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
@RequestMapping("dish")
public class DishController {

    private final DishService dishService;
    private final UserService userService;
    private final IngredientService ingredientService;


    @Autowired
    public DishController(DishService dishService, UserService userService, IngredientService ingredientService) {
        this.dishService = dishService;
        this.userService = userService;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/create")
    public ModelAndView getCreateDishRequest() {
        ModelAndView modelCreate = new ModelAndView();
        modelCreate.setViewName("create-dish");
        modelCreate.addObject("createDish", new CreateDishRequest());
        modelCreate.addObject("dishTypes", DishType.values());
        return modelCreate;
    }

    @PostMapping("/create")
    public ModelAndView getCreateDishRequest(@Valid CreateDishRequest request, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelCreate = new ModelAndView();
            modelCreate.setViewName("create-dish");
            modelCreate.addObject("createDish", request);
            modelCreate.addObject("dishTypes", DishType.values());
            modelCreate.addObject("errors", bindingResult.getAllErrors());

            return modelCreate;
        }
        User user = userService.getById(authenticationMetadata.getUserId());
        dishService.createDish(request, user);
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/editor")
    public ModelAndView getEditDishRequest() {
        ModelAndView model = new ModelAndView();
        model.setViewName("edit-dish");
        return model;
    }

    @GetMapping("/searching")
    public ModelAndView searchDishRequest(@RequestParam(name = "name", required = false) String name) {
        ModelAndView model = new ModelAndView("edit-dish");

        if (name != null && !name.isEmpty()) {
            var dish = dishService.findByName(name);
            Set<Ingredient> ingredients = dish.getIngredients();
            System.out.println("Ingredients: " + ingredients);
            if (dish != null) {
                model.addObject("dish", dish);
                model.addObject("ingredients", ingredients);
            } else {
                model.addObject("notFound", true);
            }
        }

        return model;
    }
    @PostMapping("/ingredient/new") // Двойни кавички!
    public ModelAndView getPostIngredientNewRequest(@RequestParam(name = "name", required = false) String name) {
        ModelAndView model = new ModelAndView();
        var dish = dishService.findByName(name);
       // dish.getIngredients().add()
        return model;
    }


}
