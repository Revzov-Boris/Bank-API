package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

// для создания пользователя
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @Schema(description = "Логин", example = "login")
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    private String login;

    @Schema(description = "Пароль", example = "12234qwer")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 50, message = "Пароль должен быть от 8 до 100 символов")
    private String password;

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
