package com.example.CookMaster.app.web;



import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.store.service.StoreService;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;


@Slf4j
@Controller
public class ShoppingController {

    private final UserService userService;
    private final StoreService storeService;

    @Autowired
    public ShoppingController(UserService userService, StoreService storeService) {
        this.userService = userService;
        this.storeService = storeService;
    }

    @PatchMapping("shopping-list/new")
    public ModelAndView patchUpdateStoresInfoRequest(@RequestParam("assignments") String assignments){

        storeService.updateStoresAndIngredients(assignments);

        return new ModelAndView("redirect:/profile");
    }
    @GetMapping("/shopping")
    public ModelAndView getShoppingListRequest(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata){
        ModelAndView modelAndView = new ModelAndView("shopping");
        User user = userService.getById(authenticationMetadata.getUserId());
        storeService.removeUnlinkedIngredients();
        var stores = user.getStores();

        if (!stores.isEmpty()) {
            log.info("Store ingredients: {}", stores.iterator().next().getIngredients());

        } else {
            log.info("No stores found");
            modelAndView.addObject("info", "No stores have been created yet!");
        }

        modelAndView.addObject("stores", stores);
        modelAndView.addObject("user", user);

        return modelAndView;
    }
    @PatchMapping("/shopping/products/purchase")
    public String boughtIngredientsRequest(@RequestParam ("storeName") String name) {
        storeService.deleteBoughtIngredients(name);
        return "redirect:/shopping";
    }

    @GetMapping("/users/{id}/profile/go-back")
    public String getEditProfileRequest(@PathVariable UUID id) {
        return "redirect:/profile";
    }

}
