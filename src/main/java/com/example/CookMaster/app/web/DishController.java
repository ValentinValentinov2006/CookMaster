package com.example.CookMaster.app.web;



import com.example.CookMaster.app.dish.model.DishType;
import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.ingredient.model.Ingredient;
import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.CreateDishRequest;
import com.example.CookMaster.app.web.dto.EditDishRequest;
import com.example.CookMaster.app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.UUID;

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


            if (dish != null) {
                model.addObject("dish", dish);

                model.addObject("dishRequest", DtoMapper.mapDishToEditDishRequest(dish));
            } else {
                model.addObject("notFound", true);
            }
        }

       return model;
    }


    @PatchMapping("/update/{id}")
    public ModelAndView updateUserDishRequest(@PathVariable UUID id, @Valid EditDishRequest request, BindingResult bindingResult) {
        System.out.println("Request -> "+request);


        if (bindingResult.hasErrors()) {
            ModelAndView model = new ModelAndView("edit-dish");
            model.addObject("errors", bindingResult.getAllErrors());
            return model;
        }

        dishService.editDishRequest(id, request);

        System.out.println();
        return new ModelAndView("redirect:/profile");
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteUserDishRequest(@PathVariable UUID id) {
        dishService.deleteDish(id);
        return new ModelAndView("redirect:/profile");
    }



}
