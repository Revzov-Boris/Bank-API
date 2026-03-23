package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlockingRequest {
    @NotNull(message = "ID карты-отправителя не может быть null")
    private Integer cardId;
}
