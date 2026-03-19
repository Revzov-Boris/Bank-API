package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "card_from_id", nullable = false)
    private CardEntity fromCard;
    @ManyToOne
    @JoinColumn(name = "card_to_id", nullable = false)
    private CardEntity toCard;
    @Column(precision = 12, scale = 2)
    private BigDecimal amount;
}
