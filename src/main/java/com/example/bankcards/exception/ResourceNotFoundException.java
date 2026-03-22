package com.example.bankcards.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceTitle, String identifier, String verb, Object idValue) {
        super(String.format("%s с %s = %s не %s", resourceTitle, identifier, idValue, verb));
    }
}
