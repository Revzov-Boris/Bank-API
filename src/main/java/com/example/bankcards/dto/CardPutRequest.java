package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// для PUT-запросов, можно поменять всё, кроме ID и номера карты
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardPutRequest {
    @NotNull(message = "ID пользователя не может быть пустым")
    Integer userId;
    @NotBlank(message = "Название банка не может быть пустым")
    @Size(min = 2, max = 50, message = "Название банка должно быть от 2 до 50 символов включительно")
    String bankTitle;
    @NotBlank(message = "Тип карты не может быть пустым")
    @Size(min = 2, max = 50, message = "Тип карты должен быть от 2 до 50 символов включительно")
    String type;
    @NotNull(message = "Срок действия не может быть пустым")
    LocalDate expiryDate;
    @NotNull(message = "Статус карты не может быть пустым")
    CardStatus status;
    @NotNull(message = "Баланс не может быть пустым")
    @DecimalMin(value = "0.00", message = "Баланс не может быть отрицательным")
    @DecimalMax(value = "9999999999.99", message = "Баланс не может превышать максимальное значение")
    @Digits(integer = 10, fraction = 2,
            message = "Баланс должен состоять не более чем из 10 целых цифр и 2-х знаков после запятой")
    BigDecimal balance;
}
