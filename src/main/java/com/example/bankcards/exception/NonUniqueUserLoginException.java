package com.example.bankcards.exception;

public class NonUniqueUserLoginException extends UniqueFieldException {
    public NonUniqueUserLoginException(String message) {
        super(message);
    }
}
