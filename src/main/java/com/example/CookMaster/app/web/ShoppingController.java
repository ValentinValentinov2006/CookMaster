package com.example.CookMaster.app.web;


import com.example.CookMaster.app.store.model.Store;
import com.example.CookMaster.app.store.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
public class ShoppingController {


    private final StoreService storeService;

    @Autowired
    public ShoppingController(StoreService storeService) {

        this.storeService = storeService;
    }

    @PatchMapping("shopping-list/new")
    public ModelAndView patchUpdateStoresInfoRequest(@RequestParam("assignments") String assignments){

        storeService.updateStoresAndIngredients(assignments);

        return new ModelAndView("redirect:/profile");
    }
    @GetMapping("/shopping")
    public ModelAndView getShoppingListRequest(){
        ModelAndView modelAndView = new ModelAndView("shopping");
        var stores = storeService.getAllStores();
        if (!stores.isEmpty()) {
            log.info("Store ingredients: {}", stores.iterator().next().getIngredients());

        } else {
            log.info("No stores found");
        }

        modelAndView.addObject("stores", stores);

        return modelAndView;
    }

}
