package com.example.bankcards.exception;

public class NonUniqueCardNumberException extends UniqueFieldException {
    public NonUniqueCardNumberException(String message) {super(message);}
}
