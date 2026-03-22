package com.example.bankcards.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Integer id) {
        super("Пользователь", "ID", "найден", id);
    }
}
