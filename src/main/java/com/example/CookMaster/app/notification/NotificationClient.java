package com.example.CookMaster.app.notification;


import com.example.CookMaster.app.notification.dto.NotificationRequest;
import com.example.CookMaster.app.notification.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "notification-svc", url = "http://localhost:8081/api/notifications")
public interface NotificationClient {

    @PostMapping
    void sendNotification(@RequestBody NotificationRequest request);

    @GetMapping
    List<NotificationResponse> getAllNotifications();
}
