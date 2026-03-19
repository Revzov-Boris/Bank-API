package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    // зашифрованный номер карты
    @Column(unique = true)
    private String cardNumber;
    private String bankTitle;
    private String type;
    private CardStatus status;
    @Column(precision = 12, scale = 2)
    private BigDecimal balance;
    private LocalDate expiryDate;
}
