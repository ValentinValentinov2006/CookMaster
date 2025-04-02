package com.example.CookMaster.dto;

import com.example.CookMaster.app.notification.dto.NotificationRequest;
import com.example.CookMaster.app.notification.dto.NotificationResponse;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class NotificationDtoTest {

    @Test
    void testNotificationRequest_ShouldHaveCorrectValues() {
        UUID userId = UUID.randomUUID();
        String message = "New notification!";
        String email = "test@example.com";

        NotificationRequest request = new NotificationRequest(userId, message, email);

        assertThat(request.getUserId()).isEqualTo(userId);
        assertThat(request.getMessage()).isEqualTo(message);
        assertThat(request.getEmail()).isEqualTo(email);
    }

    @Test
    void testNotificationRequest_ShouldHandleNoArgsConstructor() {
        NotificationRequest request = new NotificationRequest();
        assertThat(request).isNotNull();
        assertThat(request.getUserId()).isNull();
        assertThat(request.getMessage()).isNull();
        assertThat(request.getEmail()).isNull();
    }

    @Test
    void testNotificationResponse_ShouldHaveCorrectValues() {
        UUID id = UUID.randomUUID();
        String subject = "Test Subject";
        String body = "This is a test notification.";
        LocalDateTime createdOn = LocalDateTime.now();
        String status = "SENT";
        String type = "EMAIL";

        NotificationResponse response = new NotificationResponse(id, subject, body, createdOn, status, type);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getSubject()).isEqualTo(subject);
        assertThat(response.getBody()).isEqualTo(body);
        assertThat(response.getCreatedOn()).isEqualTo(createdOn);
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getType()).isEqualTo(type);
    }

    @Test
    void testNotificationResponse_ShouldHandleNoArgsConstructor() {
        NotificationResponse response = new NotificationResponse();
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNull();
        assertThat(response.getSubject()).isNull();
        assertThat(response.getBody()).isNull();
        assertThat(response.getCreatedOn()).isNull();
        assertThat(response.getStatus()).isNull();
        assertThat(response.getType()).isNull();
    }
}
