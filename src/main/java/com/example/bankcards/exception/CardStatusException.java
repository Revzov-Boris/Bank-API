package com.example.bankcards.exception;

import com.example.bankcards.entity.CardStatus;

public class CardStatusException extends RuntimeException {
    public CardStatusException(String message) {
        super(message);
    }

    public CardStatusException(String operation, int id, CardStatus status) {
        super(String.format("Нельзя %s карту с ID = %d, так как её статус %s", operation, id, status));
    }
}
