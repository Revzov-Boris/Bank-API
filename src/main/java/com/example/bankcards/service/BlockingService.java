package com.example.bankcards.service;

import com.example.bankcards.dto.BlockingRequest;
import com.example.bankcards.dto.BlockingResponse;

public interface BlockingService {
    BlockingResponse createBlock(BlockingRequest blocking, int userId);
    boolean hasPendingBlocking(int cardId);
}
