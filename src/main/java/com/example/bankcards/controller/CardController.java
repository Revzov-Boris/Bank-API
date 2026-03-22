package com.example.bankcards.controller;

import com.example.bankcards.dto.CardPutRequest;
import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
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
public class CardController {
    private final CardService cardService;


    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @GetMapping("/{id}")
    public EntityModel<CardResponse> getCardById(@PathVariable int id) {
        return EntityModel.of(cardService.getCardById(id));
    }


    @GetMapping
    public PagedModel<EntityModel<CardResponse>> getAllCard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) BigDecimal minBalance,
            @RequestParam(required = false) BigDecimal maxBalance,
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


    @PostMapping
    public ResponseEntity<EntityModel<CardResponse>> createCard(@Valid @RequestBody CardRequest card) {
        CardResponse cardResponse = cardService.createCard(card);
        EntityModel<CardResponse> entityModel = EntityModel.of(cardResponse);
        entityModel.add(linkTo(methodOn(CardController.class).getCardById(cardResponse.getId())).withSelfRel());
        return new ResponseEntity(entityModel, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CardResponse>> updateCard(@Valid @RequestBody CardPutRequest card,
                                                                @PathVariable Integer id) {
        CardResponse cardResponse = cardService.updateCard(card, id);
        EntityModel<CardResponse> entityModel = EntityModel.of(cardResponse);
        entityModel.add(linkTo(methodOn(CardController.class).getCardById(cardResponse.getId())).withSelfRel());
        return new ResponseEntity(entityModel, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateCard(@PathVariable Integer id) {
        cardService.deleteCard(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/{id}/block")
    public ResponseEntity<EntityModel<CardResponse>> blockCard(@PathVariable int id) {
        return cardService.blockCardById(id);
    }


    @PostMapping("/{id}/unblock")
    public ResponseEntity<EntityModel<CardResponse>> unblockCard(@PathVariable int id) {
        return cardService.unblockCardById(id);
    }
}
