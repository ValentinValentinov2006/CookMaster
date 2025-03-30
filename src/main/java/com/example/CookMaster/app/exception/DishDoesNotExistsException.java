package com.example.CookMaster.app.exception;

public class DishDoesNotExistsException extends RuntimeException {
    public DishDoesNotExistsException(String message) {
        super(message);
    }

}
