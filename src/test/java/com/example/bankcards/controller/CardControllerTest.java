package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.security.CustomUserDetailsService;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;
import java.time.LocalDate;

@WebMvcTest(
        value = CardController.class
)
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CardService cardService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private CardResponse cardResponse = CardResponse.builder()
            .id(1)
            .type("Дебетовая")
            .bankTitle("Г-Банк")
            .balance(new BigDecimal("1084.71"))
            .status(CardStatus.ACTIVE)
            .expiryDate(LocalDate.of(2030, 11, 20))
            .userId(4009)
            .lastFourDigits("9820")
            .build();

    @Test
    public void getCardByIdTest() throws Exception{
        Mockito.when(cardService.getCardById(1)).thenReturn(cardResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/cards/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Дебетовая"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankTitle").value("Г-Банк"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1084.71))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ACTIVE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiryDate").value("2030-11-20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(4009))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastFourDigits").value("9820"));
    }
}
