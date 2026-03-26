package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Для PUT-запросов, можно поменять всё, кроме логина и пароля
public class UserPutRequest {
    @Schema(description = "Имя", example = "Иван")
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;

    @Schema(description = "Фамилия", example = "Сидоров")
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String secondName;

    @Schema(description = "Отчество", example = "Петрович")
    @Size(min = 2, max = 50, message = "Отчество должно быть от 2 до 50 символов")
    private String thirdName;

    @Schema(description = "Дата рождения", example = "1995.01.18")
    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;
}
