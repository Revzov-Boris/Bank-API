package com.example.bankcards.service;

import com.example.bankcards.entity.CardEntity;

import java.math.BigDecimal;

public interface CardCheckingService {
    boolean hasUserCard(int userId, int cardId);
    void isCardsValidForTransers(CardEntity from, CardEntity to, BigDecimal money, int userId);
}
