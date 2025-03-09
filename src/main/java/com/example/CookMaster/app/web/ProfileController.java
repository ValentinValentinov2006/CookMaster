package com.example.CookMaster.app.web;

import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;

import com.example.CookMaster.app.web.dto.ProfileEditRequest;
import com.example.CookMaster.app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getEditProfilePage(@PathVariable UUID id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("editProfile");
        User user = userService.getById(id);
        mav.addObject("user", user);
        mav.addObject("profileRequest", DtoMapper.mapUserToUserEditRequest(user));
        return mav;
    }
    @PatchMapping("/{id}/profile")
    public ModelAndView updateEditProfilePage(@PathVariable UUID id, @Valid ProfileEditRequest profileEditRequest, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                ModelAndView mav = new ModelAndView("editProfile");
                mav.addObject("user", userService.getById(id));
                mav.addObject("profileRequest", profileEditRequest);
                return mav;
            }

            userService.editUserProfile(id, profileEditRequest);

        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/{id}/profile/cancel")
    public String getCancelEditProfile(@PathVariable UUID id) {
        return "redirect:/users/" + id + "/profile";
    }
}
