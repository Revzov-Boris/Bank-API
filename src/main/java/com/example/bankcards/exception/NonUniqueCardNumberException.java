package com.example.bankcards.exception;

public class NonUniqueCardNumberException extends RuntimeException {
    public NonUniqueCardNumberException(String message) {super(message);}
}
