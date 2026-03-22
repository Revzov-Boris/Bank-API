package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.math.BigDecimal;
import java.time.LocalDate;

// Возвращаемый на запрос тип
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "cards", itemRelation = "card")
@Setter
@Getter
public class CardResponse extends RepresentationModel<CardResponse> {
    private Integer id;
    private Integer userId;
    private String lastFourDigits;
    private String bankTitle;
    private CardStatus status;
    private String type;
    private LocalDate expiryDate;
    private BigDecimal balance;
}
