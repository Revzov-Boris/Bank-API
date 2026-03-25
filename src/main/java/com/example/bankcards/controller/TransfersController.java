package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.TransfersRequest;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.service.TranserService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
public class TransfersController {
    private final TranserService transfersService;
    private final UserService userService;

    @Autowired
    public TransfersController(TranserService transfersService, UserService userService) {
        this.transfersService = transfersService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<EntityModel<TransferResponse>> createTransfer(@Valid @RequestBody TransfersRequest transfer,
                                                                        Authentication auth) {
        Integer userId = userService.getIdByLogin(auth.getName());
        if (userId == null) {
            throw new UserNotFoundException("Пользователь с логином " + auth.getName() + " не найден");
        }
        TransferResponse response = transfersService.createTransfer(transfer, userId);
        return ResponseEntity.status(201).body(EntityModel.of(response));
    }
}
