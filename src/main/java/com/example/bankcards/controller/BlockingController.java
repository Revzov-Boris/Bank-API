package com.example.bankcards.controller;

import com.example.bankcards.dto.BlockingRequest;
import com.example.bankcards.dto.BlockingResponse;
import com.example.bankcards.exception.BlockingException;
import com.example.bankcards.service.BlockingService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/blockings")
public class BlockingController {
    private final BlockingService blockingService;
    private final UserService userService;

    public BlockingController(BlockingService blockingService, UserService userService) {
        this.blockingService = blockingService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<BlockingResponse>> createBlcck(@Valid @RequestBody BlockingRequest blocking) {
        // TODO определить ID вошедшего пользователя
        int userId = 1; // пока захардкожено)
        if (!userService.hasUserCard(userId, blocking.getCardId())) {
            throw new BlockingException("Вы не владелец карты с ID = " + blocking.getCardId());
        }
        BlockingResponse response = blockingService.createBlock(blocking);
        return ResponseEntity.status(200).body(EntityModel.of(response));
    }
}
