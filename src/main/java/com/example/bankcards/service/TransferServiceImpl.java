package com.example.bankcards.service;

import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.TransfersRequest;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.TransferEntity;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransfersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferServiceImpl implements TranserService {
    private final TransfersRepository transfersRepository;
    private final CardRepository cardRepository;
    private final CardCheckingService cardCheckingService;

    @Autowired
    public TransferServiceImpl(TransfersRepository transfersRepository, CardRepository cardRepository, CardCheckingService cardCheckingService) {
        this.transfersRepository = transfersRepository;
        this.cardRepository = cardRepository;
        this.cardCheckingService = cardCheckingService;
    }


    @Override
    @Transactional
    public TransferResponse createTransfer(TransfersRequest transfer, int userId) {
        if (transfer.getCardFromId().equals(transfer.getCardToId())) {
            throw new TransferException("Перевод возможен только между двумя разными картами");
        }
        // блокировка карт
        List<CardEntity> twoCards = blockCardsForTranfser(transfer.getCardFromId(), transfer.getCardToId());
        CardEntity from = transfer.getCardFromId().equals(twoCards.getFirst().getId()) ?
                twoCards.getFirst() : twoCards.getLast();
        CardEntity to = transfer.getCardToId().equals(twoCards.getFirst().getId()) ?
                twoCards.getFirst() : twoCards.getLast();
        // проверка
        cardCheckingService.isCardsValidForTransers(from, to, transfer.getMoney(), userId);
        // если всё нормально, производим перевод
        from.setBalance(from.getBalance().subtract(transfer.getMoney()));
        to.setBalance(to.getBalance().add(transfer.getMoney()));
        TransferEntity transferEntity = TransferEntity.builder()
                .fromCard(from)
                .toCard(to)
                .amount(transfer.getMoney())
                .build();
        transferEntity = transfersRepository.save(transferEntity);
        return toResponse(transferEntity);
    }

    // блокирует карты в порядке возрастания id, для исключения взаимной блокирвки
    private List<CardEntity> blockCardsForTranfser(Integer cardId1, Integer cardId2) {
        Integer firstId = Math.min(cardId1, cardId2);
        Integer secondId = Math.max(cardId1, cardId2);
        CardEntity first = cardRepository.findByIdForUpdate(firstId).orElseThrow(
                () -> new CardNotFoundException(firstId)
        );
        CardEntity second = cardRepository.findByIdForUpdate(secondId).orElseThrow(
                () -> new CardNotFoundException(secondId)
        );
        return List.of(first, second);
    }


    public TransferResponse toResponse(TransferEntity entity) {
        return TransferResponse.builder()
                .id(entity.getId())
                .money(entity.getAmount())
                .cardFromId(entity.getFromCard().getId())
                .cardToId(entity.getToCard().getId())
                .build();
    }
}
