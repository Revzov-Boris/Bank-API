package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Для Patch запросов
// Можно изменить все значения, кроме номера карты
public record CardPatchRequest(
        Integer userId,
        @Size(min = 2, max = 50, message = "Название банка должно быть от 2 до 50 символов включительно")
        String bankTitle,
        @Size(min = 2, max = 50, message = "Тип карты должен быть от 2 до 50 символов включительно")
        String type,
        LocalDate expiryDate,
        CardStatus status,
        @DecimalMin(value = "0.00", message = "Баланс не может быть отрицательным")
        @DecimalMax(value = "9999999999.99", message = "Баланс не может превышать максимальное значение")
        BigDecimal balance
) {
}
