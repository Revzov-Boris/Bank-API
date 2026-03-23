package com.example.bankcards.service;

import com.example.bankcards.dto.BlockingRequest;
import com.example.bankcards.dto.BlockingResponse;

public interface BlockingService {
    BlockingResponse createBlock(BlockingRequest blocking);
    boolean hasPendingBlocking(int cardId);
}
