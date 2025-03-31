package com.example.CookMaster.app.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequest {

    @NotNull
    @Size(min = 3, max = 20, message = "Please write a valid store name!")
    private String storeName;


    private String address;

    @Size(min = 10, max = 10, message = "Phne number must be exactly 9 digits")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and be exactly 9 digits")
    private String phoneNumber;


    private String notes;
}
