package com.example.CookMaster.app.exception;

public class NotHaveEnoughDishes extends RuntimeException {
    public NotHaveEnoughDishes(String message) {
        super(message);
    }
}
