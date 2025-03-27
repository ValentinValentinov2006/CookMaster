package com.example.CookMaster.app.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private UUID userId;
    private String message;
    private String email;
}
