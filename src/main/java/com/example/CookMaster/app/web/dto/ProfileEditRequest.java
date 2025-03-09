package com.example.CookMaster.app.web.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class ProfileEditRequest {

    @URL(message = "Invalid URL!")
    private String url;

    @Size(min = 2, max = 20, message = "First name length must be between 2 and 20 characters!")
    private String firstName;

    @Size(min = 2, max = 30, message = "Last name length must be between 2 and 30 characters!")
    private String lastName;

    @Email(message = "Invalid Email!")
    private String email;
}
