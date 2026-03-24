package com.example.bankcards.service;

import com.example.bankcards.dto.BlockingRequest;
import com.example.bankcards.dto.BlockingResponse;
import com.example.bankcards.entity.BlockingEntity;
import com.example.bankcards.entity.BlockingStatus;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.BlockingException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.BlockingRepository;
import com.example.bankcards.repository.CardRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;

@Service
public class BlockingServiceImpl implements BlockingService {
    private final BlockingRepository blockingRepository;
    private final CardRepository cardRepository;
    private final CardCheckingService cardCheckingService;

    public BlockingServiceImpl(BlockingRepository blockingRepository, CardRepository cardRepository, CardCheckingService cardCheckingService) {
        this.blockingRepository = blockingRepository;
        this.cardRepository = cardRepository;
        this.cardCheckingService = cardCheckingService;
    }


    @Override
    @Transactional
    public BlockingResponse createBlock(@Valid @RequestBody BlockingRequest blocking, int userId) {
        // блокируем карту
        CardEntity cardEntity = cardRepository.findByIdForUpdate(blocking.getCardId()).orElseThrow(
                () -> new CardNotFoundException(blocking.getCardId())
        );
        if (!cardCheckingService.hasUserCard(userId, blocking.getCardId())) {
            throw new BlockingException("Вы не владелец карты с ID = " + blocking.getCardId());
        }
        if (!cardEntity.getStatus().equals(CardStatus.ACTIVE)) {
            throw new BlockingException("Карта с ID = " + blocking.getCardId() + " не активна, нельзя создать запрос на блокировку");
        }
        if (hasPendingBlocking(blocking.getCardId())) {
            throw new BlockingException("У карты с ID = " + blocking.getCardId() + " уже есть блокировка со статусом PENDING");
        }
        BlockingEntity blockingEntity = BlockingEntity.builder()
                .card(cardEntity)
                .dateTime(LocalDateTime.now())
                .status(BlockingStatus.PENDING)
                .build();
        blockingEntity = blockingRepository.save(blockingEntity);
        return toResponse(blockingEntity);
    }

    @Override
    public boolean hasPendingBlocking(int cardId) {
        return blockingRepository.findCountByCardAndStatus(cardId, BlockingStatus.PENDING) > 0;
    }


    public BlockingResponse toResponse(BlockingEntity entity) {
        return new BlockingResponse(entity.getId(), entity.getCard().getId(), entity.getDateTime());
    }
}
