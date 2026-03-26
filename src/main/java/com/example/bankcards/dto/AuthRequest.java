package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Логин не может быть пустым")
    @Schema(description = "Логин", example = "MyLogin")
    private String login;
    @NotNull
    @Schema(description = "Пароль", example = "123qwert")
    @Size(min = 8, message = "Пароль должен быть от 8 символов")
    private String password;
}
