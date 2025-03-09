package com.example.CookMaster.app.web;


import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import com.example.CookMaster.app.web.dto.LogInRequest;
import com.example.CookMaster.app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView getIndexPage() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView loginRequest(@RequestParam(value = "error", required = false) String error) {
        ModelAndView loginModel = new ModelAndView();

        if (error != null) {
            loginModel.addObject("errorMessage", "Incorrect username or password!");
        }

        loginModel.addObject("loginRequest", new LogInRequest());
        loginModel.setViewName("login");

        return loginModel;
    }



    @GetMapping("/signup")
    public ModelAndView registerRequest() {
        ModelAndView registerModel = new ModelAndView();
        registerModel.addObject("registerRequest", new RegisterRequest());
        registerModel.setViewName("signup");
        return registerModel;
    }
    @PostMapping("/signup")
    public ModelAndView signupRequest(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
           return new ModelAndView("signup");
        }
        userService.registerUser(registerRequest);

        return new ModelAndView("redirect:/login");
    }
    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView profileModel = new ModelAndView();
        User user = userService.getById(authenticationMetadata.getUserId());
        profileModel.addObject("user", user);
        profileModel.setViewName("profile");
        return profileModel;
    }
}