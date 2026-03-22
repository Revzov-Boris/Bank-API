package com.example.bankcards.service;

import com.example.bankcards.dto.CardPutRequest;
import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.CardStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

public interface CardService {
    Page<CardResponse> getAllCards(Pageable pageable);
    CardResponse getCardById(int id);
    Page<CardResponse> findAllWithFilters(Pageable pageable,
                                          Integer userId,
                                          CardStatus status,
                                          BigDecimal minBalance,
                                          BigDecimal maxBalance);

    Page<CardResponse> findByBalanceRangeAndStatus(Pageable pageable,
                                                   BigDecimal minBalance,
                                                   BigDecimal maxBalance,
                                                   CardStatus status);

    Page<CardResponse> findByStatus(Pageable pageable, CardStatus status);
    Page<CardResponse> findByBalanceRange(Pageable pageable, BigDecimal minBalance, BigDecimal maxBalance);
    CardResponse createCard(CardRequest card);
    CardResponse updateCard(@Valid CardPutRequest card, int id);
    void deleteCard(int id);
    ResponseEntity<EntityModel<CardResponse>> blockCardById(int id);
    ResponseEntity<EntityModel<CardResponse>> unblockCardById(int id);
}
