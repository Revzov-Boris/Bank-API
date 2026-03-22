package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

// для создания пользователя
public class UserRequest {
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 50, message = "Пароль должен быть от 6 до 100 символов")
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    @Size(max = 50, message = "Отчество должно быть не более 50 символов")
    private String thirdName;

    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;
}
