package com.example.CookMaster.app.web;

import com.example.CookMaster.app.ingredient.service.IngredientService;
import com.example.CookMaster.app.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("shopping-list")
public class ShoppingController {


    private final StoreService storeService;

    @Autowired
    public ShoppingController(StoreService storeService) {

        this.storeService = storeService;
    }

    @PatchMapping("/new")
    public ModelAndView patchUpdateStoresInfoRequest(@RequestParam("assignments") String assignments){

        storeService.updateStoresAndIngredients(assignments);

        return new ModelAndView("redirect:/profile");


    }
}
