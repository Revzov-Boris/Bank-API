package com.example.bankcards.service;

import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.TransfersRequest;

public interface TranserService {
    TransferResponse createTransfer(TransfersRequest transfers, int userId);
}
