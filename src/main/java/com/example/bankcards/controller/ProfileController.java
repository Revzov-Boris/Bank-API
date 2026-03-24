package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;


@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final CardService cardService;

    @Autowired
    public ProfileController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<EntityModel<UserResponse>> getMyPage() {
        //TODO определять id вошедшего пользователя
        int userId = 5;
        return ResponseEntity.status(200)
                .body(EntityModel.of(userService.getUserById(userId)));
    }


    @GetMapping("/myCards")
    public PagedModel<EntityModel<CardResponse>> getMyCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) BigDecimal minBalance,
            @RequestParam(required = false) BigDecimal maxBalance
    ) {
        //TODO определять id вошедшего пользователя
        int userId = 5;

        Pageable pageable = PageRequest.of(page, size);
        Page<CardResponse> pageCards = cardService.findAllWithFilters(pageable, userId, status, minBalance, maxBalance);
        PagedModel<EntityModel<CardResponse>> pageModel = PagedModel.of(
                pageCards.stream().map(c -> {
                    EntityModel<CardResponse> entityModel = EntityModel.of(c);
                    return entityModel;

                }).toList(),
                new PagedModel.PageMetadata(pageCards.getSize(), pageCards.getNumber(), pageCards.getTotalElements(), pageCards.getTotalPages())
        );
        // Добавляем HATEOAS ссылки
        // ссылка на себя
        pageModel.add(Link.of(linkBuild(page, size, status, minBalance, maxBalance)).withSelfRel());
        if (pageCards.hasNext()) {
            pageModel.add(Link.of(linkBuild(page + 1, size, status, minBalance, maxBalance))
                    .withRel("next"));
        }
        if (pageCards.hasPrevious()) {
            pageModel.add(Link.of(linkBuild(page - 1, size, status, minBalance, maxBalance))
                    .withRel("prev"));
        }
        return pageModel;

    }


    private String linkBuild(int page,
                             int size,
                             CardStatus status,
                             BigDecimal minBalance,
                             BigDecimal maxBalance) {
        StringBuilder path = new StringBuilder("/profile/myCards?page=" + page + "&size=" + size);
        if (status != null) {
            path.append("&status=").append(status);
        }
        if (minBalance != null) {
            path.append("&minBalance=").append(minBalance);
        }
        if (maxBalance != null) {
            path.append("&maxBalance=").append(maxBalance);
        }
        return path.toString();
    }
}
