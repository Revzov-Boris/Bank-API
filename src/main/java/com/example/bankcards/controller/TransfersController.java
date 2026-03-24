package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.TransfersRequest;
import com.example.bankcards.service.TranserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
public class TransfersController {
    private final TranserService transfersService;

    @Autowired
    public TransfersController(TranserService transfersService) {
        this.transfersService = transfersService;
    }


    @PostMapping
    public ResponseEntity<EntityModel<TransferResponse>> createTransfer(@Valid @RequestBody TransfersRequest transfer) {
        //TODO определить ID вошедшего пользователя
        int userId = 1;
        TransferResponse response = transfersService.createTransfer(transfer, userId);
        return ResponseEntity.status(201).body(EntityModel.of(response));
    }
}
