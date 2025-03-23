package com.example.CookMaster.app.web.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data

public class MenuRequest {

    private String dayOfWeek;

    @NotNull
    private UUID breakfastId;

    @NotNull
    private UUID lunchId;

    @NotNull
    private UUID dinnerId;
}
