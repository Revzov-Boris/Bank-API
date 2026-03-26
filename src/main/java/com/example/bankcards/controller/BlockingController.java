package com.example.bankcards.controller;

import com.example.bankcards.dto.BlockingRequest;
import com.example.bankcards.dto.BlockingResponse;
import com.example.bankcards.exception.BlockingException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.service.BlockingService;
import com.example.bankcards.service.CardCheckingService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Blockings", description = "Управление запросами на блокировку")
@RestController
@RequestMapping("/blockings")
public class BlockingController {
    private final BlockingService blockingService;
    private final UserService userService;

    @Autowired
    public BlockingController(BlockingService blockingService, UserService userService) {
        this.blockingService = blockingService;
        this.userService = userService;
    }

    @Operation(summary = "Создать запрос на блокировку карты по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Зарос создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании запроса")
    })
    @PostMapping
    public ResponseEntity<EntityModel<BlockingResponse>> createBlcck(@Valid @RequestBody BlockingRequest blocking,
                                                                     Authentication auth) {
        Integer userId = userService.getIdByLogin(auth.getName());
        if (userId == null) {
            throw new UserNotFoundException("Пользователь с логином " + auth.getName() + " не найден");
        }
        BlockingResponse response = blockingService.createBlock(blocking, userId);
        return ResponseEntity.status(200).body(EntityModel.of(response));
    }
}
