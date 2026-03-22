package com.example.bankcards.service;

import com.example.bankcards.dto.CardPutRequest;
import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.CardStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    Page<CardResponse> getAllCards(Pageable pageable);
    CardResponse getCardById(int id);
    Page<CardResponse> findByBalanceRangeAndStatus(Pageable pageable, BigDecimal minBalance, BigDecimal maxBalance, CardStatus status);
    Page<CardResponse> findByStatus(Pageable pageable, CardStatus status);
    Page<CardResponse> findByBalanceRange(Pageable pageable, BigDecimal minBalance, BigDecimal maxBalance);
    CardResponse createCard(CardRequest card);
    CardResponse updateCard(@Valid CardPutRequest card, int id);
    void deleteCard(int id);
}
