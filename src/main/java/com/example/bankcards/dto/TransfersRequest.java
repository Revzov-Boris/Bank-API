package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransfersRequest {
    @Schema(description = "ID карты-отправителя", example = "1")
    @NotNull
    private Integer cardFromId;
    @NotNull
    @Schema(description = "ID карты-получателя", example = "2")
    private Integer cardToId;
    @Schema(description = "Сумма перевода", example = "200")
    @NotNull(message = "Сумма перевод не может быть пустым")
    @DecimalMin(value = "0.01", message = "Сумма перевода должна составлять хотя бы 1 копейку")
    @DecimalMax(value = "9999999999.99", message = "Сумма перевода не может превышать максимальное значение 9999999999.99")
    @Digits(integer = 10, fraction = 2,
            message = "Сумма перевода должна состоять не более чем из 10 целых цифр и 2-х знаков после запятой")
    private BigDecimal money;
}
