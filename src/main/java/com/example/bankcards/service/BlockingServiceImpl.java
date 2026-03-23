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

    public BlockingServiceImpl(BlockingRepository blockingRepository, CardRepository cardRepository) {
        this.blockingRepository = blockingRepository;
        this.cardRepository = cardRepository;
    }


    @Override
    @Transactional
    public BlockingResponse createBlock(@Valid @RequestBody BlockingRequest blocking) {
        CardEntity cardEntity = cardRepository.findById(blocking.getCardId()).orElseThrow(
                () -> new CardNotFoundException(blocking.getCardId())
        );
        if (hasPendingBlocking(blocking.getCardId())) {
            throw new BlockingException("У карты с ID = " + blocking.getCardId() + " уже есть блокировка со статусом PENDING");
        }
        if (cardEntity.getStatus().equals(CardStatus.EXPIRED)) {
            throw new BlockingException("Карта с ID = " + blocking.getCardId() + " просрочена, нельзя создавать для неё запросы на блокировку");
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
    @Transactional
    public boolean hasPendingBlocking(int cardId) {
        return blockingRepository.findCountByCardAndStatus(cardId, BlockingStatus.PENDING) > 0;
    }


    public BlockingResponse toResponse(BlockingEntity entity) {
        return new BlockingResponse(entity.getId(), entity.getCard().getId(), entity.getDateTime());
    }
}
