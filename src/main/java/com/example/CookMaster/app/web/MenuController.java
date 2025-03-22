package com.example.CookMaster.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MenuController {

    @GetMapping("/menu")
    public ModelAndView getMenuRequest(@RequestParam(name = "day", required = false) Integer dayIndex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("menu");
        modelAndView.addObject("dayIndex", dayIndex);
        return modelAndView;
    }
}
