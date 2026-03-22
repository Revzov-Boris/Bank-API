package com.example.bankcards.service;

import com.example.bankcards.dto.CardPutRequest;
import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.NonUniqueCardNumberException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberEncryption;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberEncryption encrypter;
    //private final ModelMapper modelMapper;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository,
                           UserRepository userRepository,
                           CardNumberEncryption encrypter) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.encrypter = encrypter;
        //this.modelMapper = modelMapper;
    }


    @Override
    public Page<CardResponse> getAllCards(Pageable pageable) {
        Page<CardResponse> page = cardRepository.findAll(pageable).map(e -> toResponse(e));
        return page;
    }


    @Override
    public CardResponse getCardById(int id) {
        CardEntity cardEntity =  cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(id)
        );
        return toResponse(cardEntity);
    }


    @Override
    public Page<CardResponse> findByBalanceRangeAndStatus(Pageable pageable, BigDecimal minBalance, BigDecimal maxBalance, CardStatus status) {
        return cardRepository.findByBalanceBetweenAndStatus(minBalance, maxBalance, status, pageable)
                .map(e -> toResponse(e));
    }


    @Override
    public Page<CardResponse> findByStatus(Pageable pageable, CardStatus status) {
        return cardRepository.findByStatus(pageable, status).map(e -> toResponse(e));
    }


    @Override
    public Page<CardResponse> findByBalanceRange(Pageable pageable, BigDecimal minBalance, BigDecimal maxBalance) {
        Page<CardResponse> page = cardRepository.findByBalanceBetween(pageable, minBalance, maxBalance).map(e -> toResponse(e));
        return page;
    }


    @Override
    @Transactional
    public CardResponse createCard(CardRequest card) {
        UserEntity userEntity = userRepository.findById(card.getUserId()).orElseThrow(
                () -> new UserNotFoundException(card.getUserId())
        );

        String encryptionNumber = encrypter.encrypt(card.getCardNumber());
        if (cardRepository.findByCardNumber(encryptionNumber).isPresent()) {
            throw new NonUniqueCardNumberException("Попытка создать карту с занятым номером");
        }

        CardEntity cardEntity = CardEntity.builder()
                .user(userEntity)
                .lastFourDigits(card.getCardNumber().substring(12))
                .balance(card.getBalance())
                .type(card.getType())
                .status(card.getStatus())
                .cardNumber(encryptionNumber)
                .expiryDate(card.getExpiryDate())
                .bankTitle(card.getBankTitle())
                .build();
        CardEntity createdCard = cardRepository.save(cardEntity);
        return toResponse(createdCard);
    }


    @Override
    @Transactional
    public CardResponse updateCard(CardPutRequest card, int id) {
        CardEntity cardEntity = cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(id)
        );
        UserEntity userEntity = userRepository.findById(card.getUserId()).orElseThrow(
                () -> new UserNotFoundException(card.getUserId())
        );
        cardEntity.setUser(userEntity);
        cardEntity.setBankTitle(card.getBankTitle());
        cardEntity.setType(card.getType());
        cardEntity.setStatus(card.getStatus());
        cardEntity.setBalance(card.getBalance());
        cardEntity.setExpiryDate(card.getExpiryDate());
        CardEntity updatedCard = cardRepository.save(cardEntity);
        return toResponse(updatedCard);
    }


    @Override
    @Transactional
    public void deleteCard(int id) {
        CardEntity cardEntity = cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(id)
        );
        cardRepository.delete(cardEntity);
    }

    public CardResponse toResponse(CardEntity e) {
        return CardResponse.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .bankTitle(e.getBankTitle())
                .status(e.getStatus())
                .type(e.getType())
                .balance(e.getBalance())
                .expiryDate(e.getExpiryDate())
                .lastFourDigits(e.getLastFourDigits())
                .build();
    }
}
