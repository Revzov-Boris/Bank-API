package com.example.bankcards.controller;

import com.example.bankcards.dto.CardPutRequest;
import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.CardPatchRequest;
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
            @RequestParam(required = false) BigDecimal maxBalance
            ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CardResponse> pageCards;
        System.out.println(String.format("status = %s\nminBalance = %s\nmaxBalance = %s", status, minBalance, maxBalance));
        if (maxBalance != null && minBalance != null && status != null) {
            pageCards = cardService.findByBalanceRangeAndStatus(pageable, minBalance, maxBalance, status);
        } else if (maxBalance != null && minBalance != null) {
            pageCards = cardService.findByBalanceRange(pageable, minBalance, maxBalance);
        } else if (status != null) {
            pageCards = cardService.findByStatus(pageable, status);
        } else {
            pageCards = cardService.getAllCards(pageable);
        }

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
        pageModel.add(Link.of(path.toString()).withSelfRel());

        if (pageCards.hasNext()) {
            pageModel.add(linkTo(methodOn(CardController.class)
                    .getAllCard(page + 1, size, status, minBalance, maxBalance))
                    .withRel("next"));
        }

        if (pageCards.hasPrevious()) {
            pageModel.add(linkTo(methodOn(CardController.class)
                    .getAllCard(page - 1, size, status, minBalance, maxBalance))
                    .withRel("prev"));
        }

        return pageModel;
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
    public ResponseEntity<CardResponse> updateCard(@PathVariable Integer id) {
        cardService.deleteCard(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
