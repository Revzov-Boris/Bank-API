package com.example.bankcards.controller;

import com.example.bankcards.dto.CardPutRequest;
import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.PagedModel;
import java.math.BigDecimal;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cards")
@Tag(name = "Cards", description = "Управление банковскими картами")
public class CardController {
    private final CardService cardService;


    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @Operation(summary = "Получить карту по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта найдена"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @GetMapping("/{id}")
    public EntityModel<CardResponse> getCardById(@Parameter(description = "ID карты", example = "8")
                                                 @PathVariable int id) {
        return EntityModel.of(cardService.getCardById(id));
    }

    @Operation(summary = "Получить список карт с фильтрацией")
    @ApiResponse(responseCode = "200", description = "Список карт")
    @GetMapping
    public PagedModel<EntityModel<CardResponse>> getAllCard(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "4")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Статус карты", example = "ACTIVE")
            @RequestParam(required = false) CardStatus status,
            @Parameter(description = "Минимальный баланс", example = "250.00")
            @RequestParam(required = false) BigDecimal minBalance,
            @Parameter(description = "Максимальный баланс", example = "250.00")
            @RequestParam(required = false) BigDecimal maxBalance,
            @Parameter(description = "ID владельца", example = "1")
            @RequestParam(required = false) Integer userId
            ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CardResponse> pageCards = cardService.findAllWithFilters(pageable, userId, status, minBalance, maxBalance);
        PagedModel<EntityModel<CardResponse>> pageModel = PagedModel.of(
            pageCards.stream().map(c -> {
                EntityModel<CardResponse> entityModel = EntityModel.of(c);
                entityModel.add(linkTo(methodOn(CardController.class)
                        .getCardById(c.getId()))
                        .withSelfRel());
                return entityModel;

            }).toList(),
            new PagedModel.PageMetadata(pageCards.getSize(), pageCards.getNumber(), pageCards.getTotalElements(), pageCards.getTotalPages())
        );

        // Добавляем HATEOAS ссылки
        // ссылка на себя
        pageModel.add(Link.of(linkBuild(page, size, status, minBalance, maxBalance, userId)).withSelfRel());
        if (pageCards.hasNext()) {
            pageModel.add(Link.of(linkBuild(page + 1, size, status, minBalance, maxBalance, userId))
                    .withRel("next"));
        }
        if (pageCards.hasPrevious()) {
            pageModel.add(Link.of(linkBuild(page - 1, size, status, minBalance, maxBalance, userId))
                    .withRel("prev"));
        }
        return pageModel;
    }


    private String linkBuild(int page,
                             int size,
                             CardStatus status,
                             BigDecimal minBalance,
                             BigDecimal maxBalance,
                             Integer userId) {
        StringBuilder path = new StringBuilder("/cards?page=" + page + "&size=" + size);
        if (status != null) {
            path.append("&status=").append(status);
        }
        if (minBalance != null) {
            path.append("&minBalance=").append(minBalance);
        }
        if (maxBalance != null) {
            path.append("&maxBalance=").append(maxBalance);
        }
        if (userId != null) {
            path.append("&userId=" + userId);
        }
        return path.toString();
    }

    @Operation(summary = "Создать карту")
    @ApiResponse(responseCode = "201", description = "Новая карта создана")
    @ApiResponse(responseCode = "400", description = "Ошибка при создании карты")
    @PostMapping
    public ResponseEntity<EntityModel<CardResponse>> createCard(@Valid @RequestBody CardRequest card) {
        CardResponse cardResponse = cardService.createCard(card);
        EntityModel<CardResponse> entityModel = EntityModel.of(cardResponse);
        entityModel.add(linkTo(methodOn(CardController.class).getCardById(cardResponse.getId())).withSelfRel());
        return new ResponseEntity(entityModel, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить карту")
    @ApiResponse(responseCode = "200", description = "Карта обновлена")
    @ApiResponse(responseCode = "400", description = "Ошибка при обновлении карты")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CardResponse>> updateCard(@Valid @RequestBody CardPutRequest card,
                                                                @PathVariable Integer id) {
        CardResponse cardResponse = cardService.updateCard(card, id);
        EntityModel<CardResponse> entityModel = EntityModel.of(cardResponse);
        entityModel.add(linkTo(methodOn(CardController.class).getCardById(cardResponse.getId())).withSelfRel());
        return new ResponseEntity(entityModel, HttpStatus.OK);
    }
    @Operation(summary = "Удалить карту")
    @ApiResponse(responseCode = "200", description = "Карта, её переводы и запросы на блокировку  удалены")
    @ApiResponse(responseCode = "400", description = "Ошибка при обновлении карты")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateCard(@PathVariable Integer id) {
        cardService.deleteCard(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Заблокировать карту")
    @ApiResponse(responseCode = "200", description = "Карта заблокирована")
    @ApiResponse(responseCode = "204", description = "Карта и так заблокирована")
    @ApiResponse(responseCode = "400", description = "Ошибка при блокировке карты")
    @PostMapping("/{id}/block")
    public ResponseEntity<EntityModel<CardResponse>> blockCard(@PathVariable int id) {
        return cardService.blockCardById(id);
    }

    @Operation(summary = "Разблокировать карту")
    @ApiResponse(responseCode = "200", description = "Карта разблокирована")
    @ApiResponse(responseCode = "204", description = "Карта и так активна")
    @ApiResponse(responseCode = "400", description = "Ошибка при разблокировке карты")
    @PostMapping("/{id}/unblock")
    public ResponseEntity<EntityModel<CardResponse>> unblockCard(@PathVariable int id) {
        return cardService.unblockCardById(id);
    }
}
