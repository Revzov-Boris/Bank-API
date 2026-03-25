package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotNull
    private String login;
    @NotNull
    @Size(min = 8, message = "Пароль должен быть от 8 символов")
    private String password;
}
