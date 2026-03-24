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
        CardEntity from = cardRepository.findByIdForUpdate(transfer.getCardFromId()).orElseThrow(
                () -> new CardNotFoundException(transfer.getCardFromId())
        );
        CardEntity to = cardRepository.findByIdForUpdate(transfer.getCardToId()).orElseThrow(
                () -> new CardNotFoundException(transfer.getCardToId())
        );
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


    public TransferResponse toResponse(TransferEntity entity) {
        return TransferResponse.builder()
                .id(entity.getId())
                .money(entity.getAmount())
                .cardFromId(entity.getFromCard().getId())
                .cardToId(entity.getToCard().getId())
                .build();
    }
}
