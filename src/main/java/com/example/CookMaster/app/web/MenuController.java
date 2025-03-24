package com.example.CookMaster.app.web;


import com.example.CookMaster.app.calendar.model.Menu;
import com.example.CookMaster.app.calendar.service.MenuService;
import com.example.CookMaster.app.dish.model.Dish;
import com.example.CookMaster.app.dish.service.DishService;
import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.MenuRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("menu")
public class MenuController {
    private final DishService dishService;
    private final UserService userService;
    private final MenuService menuService;

    @Autowired
    public MenuController(DishService dishService, UserService userService, MenuService menuService) {
        this.dishService = dishService;
        this.userService = userService;
        this.menuService = menuService;
    }

    @GetMapping
    public ModelAndView getMenuRequest(@RequestParam(name = "day", required = false) String dayName
     , @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("menu");

        User user = userService.getById(authenticationMetadata.getUserId());
        Set<Dish> dishes = user.getDishes();

        String day = dishService.convertDays(dayName);
        Menu menu =  menuService.findMenuByDay(day);


        List<Dish> breakfastDishes = dishService.getBreakfastDishes(dishes);
        List<Dish> lunchDishes = dishService.getLunchDishes(dishes);
        List<Dish> dinnerDishes = dishService.getDinnerDishes(dishes);

        modelAndView.addObject("menu", menu);
        modelAndView.addObject("breakfastDishes", breakfastDishes);
        modelAndView.addObject("lunchDishes", lunchDishes);
        modelAndView.addObject("dinnerDishes", dinnerDishes);
        modelAndView.addObject("dayName", day);
        modelAndView.addObject("menuRequest", new MenuRequest());


        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView saveMenu(@Valid MenuRequest menuRequest,
                           BindingResult bindingResult,
                                 @RequestParam(name = "day", required = false) String dayName,
                           @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        if (menuRequest.getBreakfastId() == null || menuRequest.getLunchId() == null || menuRequest.getDinnerId() == null) {
            bindingResult.rejectValue("breakfastId", "error.menuRequest", "Breakfast must be selected!");
            bindingResult.rejectValue("lunchId", "error.menuRequest", "Lunch must be selected!");
            bindingResult.rejectValue("dinnerId", "error.menuRequest", "Dinner must be selected!");
        }



            if (bindingResult.hasErrors()) {
                User user = userService.getById(authenticationMetadata.getUserId());
                Set<Dish> dishes = user.getDishes();


                List<Dish> breakfastDishes = dishService.getBreakfastDishes(dishes);
                List<Dish> lunchDishes = dishService.getLunchDishes(dishes);
                List<Dish> dinnerDishes = dishService.getDinnerDishes(dishes);


                ModelAndView modelAndView = new ModelAndView("menu");
                modelAndView.addObject("breakfastDishes", breakfastDishes);
                modelAndView.addObject("lunchDishes", lunchDishes);
                modelAndView.addObject("dinnerDishes", dinnerDishes);
                System.out.println("Day In Error: " + menuRequest.getDayOfWeek());
                modelAndView.addObject("dayName",menuRequest.getDayOfWeek());
                modelAndView.addObject("menuRequest", menuRequest);

                return modelAndView;

        }
        User user = userService.getById(authenticationMetadata.getUserId());

        Dish breakfast = dishService.findDishNyId(menuRequest.getBreakfastId());
        Dish lunch = dishService.findDishNyId(menuRequest.getLunchId());
        Dish dinner = dishService.findDishNyId(menuRequest.getDinnerId());

        System.out.println(breakfast.getName() + " " + dinner.getName() + " " + lunch.getName());


        menuService.createMenu(breakfast, lunch, dinner, user, menuRequest);


        return new ModelAndView("redirect:/calendar");
    }
}
