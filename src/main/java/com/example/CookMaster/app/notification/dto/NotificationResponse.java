package com.example.CookMaster.app.notification.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private UUID id;
    private String subject;
    private String body;
    private LocalDateTime createdOn;
    private String status;
    private String type;
}
