package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

// Для POST запроса
@Schema(description = "Запрос на создание карты")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
        @Schema(description = "ID владельца карты", example = "1")
        @NotNull(message = "ID пользователя не может быть пустым")
        Integer userId;

        // при создании используем полный не зашифрованных номер
        @Schema(description = "Номер карты", example = "1111222233334444")
        @NotBlank(message = "Номер карты не может быть пустым")
        @Pattern(regexp = "\\d{16}", message = "Номер карты должен состоять из 16 цифр")
        String cardNumber;

        @NotBlank(message = "Название банка не может быть пустым")
        @Size(min = 2, max = 50, message = "Название банка должно быть от 2 до 50 символов включительно")
        @Schema(description = "Название банка", example = "Мой банк")
        String bankTitle;

        @Schema(description = "Тип карты", example = "дебетовая")
        @NotBlank(message = "Тип карты не может быть пустым")
        @Size(min = 2, max = 50, message = "Тип карты должен быть от 2 до 50 символов включительно")
        String type;

        @Schema(description = "Срок действия карты", example = "2030-08-01")
        @NotNull(message = "Срок действия не может быть пустым")
        LocalDate expiryDate;

        @Schema(description = "Статус карты", example = "ACTIVE")
        @NotNull(message = "Статус карты не может быть пустым")
        CardStatus status;

        @Schema(description = "Баланс", example = "468.20")
        @NotNull(message = "Баланс не может быть пустым")
        @DecimalMin(value = "0.00", message = "Баланс не может быть отрицательным")
        @DecimalMax(value = "9999999999.99", message = "Баланс не может превышать максимальное значение")
        @Digits(integer = 10, fraction = 2,
                message = "Баланс должен состоять не более чем из 10 целых цифр и 2-х знаков после запятой")
        BigDecimal balance;
}
