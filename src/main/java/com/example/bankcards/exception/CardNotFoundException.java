package com.example.bankcards.exception;

public class CardNotFoundException extends ResourceNotFoundException {
    public CardNotFoundException(String message) {
        super(message);
    }


    public CardNotFoundException(Integer id) {
        super("Карта", "ID", "найдена", id);
    }
}
