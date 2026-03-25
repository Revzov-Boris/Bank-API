package com.example.bankcards.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";

    public AuthResponse(String token) {
        this.token = token;
    }
}
