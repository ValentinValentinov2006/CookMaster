package com.example.CookMaster.app.web;

import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    private final UserService userService;

    @Autowired
    public CalendarController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getCalendarRequest(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationMetadata.getUserId());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("calendar");
        return modelAndView;
    }
    @GetMapping("/users/{id}/profile/go-back")
    public String getEditProfileRequest(@PathVariable UUID id) {
        return "redirect:/profile";
    }

}
