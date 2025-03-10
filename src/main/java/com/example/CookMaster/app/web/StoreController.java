package com.example.CookMaster.app.web;

import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.store.service.StoreService;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.CreateStoreRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("store")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    @Autowired
    public StoreController(StoreService storeService, UserService userService) {
        this.storeService = storeService;
        this.userService = userService;
    }


    @GetMapping
    public ModelAndView getStorePageRequest() {
        ModelAndView modelStore = new ModelAndView();
        modelStore.setViewName("store");
        modelStore.addObject("createStore", new CreateStoreRequest());
        return modelStore;
    }

    @PostMapping("/new")
    public ModelAndView postStoreDetailsRequest(@Valid CreateStoreRequest createStoreRequest, BindingResult bindingResult){
       if (bindingResult.hasErrors()) {
           ModelAndView modelStore = new ModelAndView();
           modelStore.setViewName("store");
           modelStore.addObject("createStore", createStoreRequest);
           modelStore.addObject("errors", bindingResult.getAllErrors());
           return modelStore;
       }
        System.out.println(createStoreRequest);
       storeService.createStore(createStoreRequest);

        return new ModelAndView("redirect:/profile");
    }
}
