package com.example.CookMaster.app.web;


import com.example.CookMaster.app.notification.NotificationClient;
import com.example.CookMaster.app.notification.dto.NotificationRequest;
import com.example.CookMaster.app.security.AuthenticationMetadata;
import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("admin")
public class AdminController {

    private final NotificationClient notificationClient;
    private final UserService userService;

    @Autowired
    public AdminController(NotificationClient notificationClient, UserService userService) {
        this.notificationClient = notificationClient;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view-users")
    public ModelAndView getUsersAdminEmailTableRequest(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        System.out.println();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("view-users");

        User user = userService.getById(authenticationMetadata.getUserId());


        List<User> users = userService.getAllUsers();
        mav.addObject("users", users);
        mav.addObject("current", user);

        System.out.println();
        return mav;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/user/{id}/status/new")
    public String changeUserStatusAdminRequest(@PathVariable UUID id) {

        userService.changeStatus(id);

        return "redirect:/admin/view-users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/profile/go-back")
    public String returnProfileRequest(@PathVariable UUID id) {
        return "redirect:/profile";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/send-notification")
    public String sendNotification(@PathVariable UUID id) {

        User user = userService.getById(id);
        String email = user.getEmail();

        System.out.println("CookMaster UUID" + id);
        NotificationRequest request = new NotificationRequest(id, "Hello, world!", email);
        notificationClient.sendNotification(request);


        return "redirect:/admin/view-users";
    }

}
