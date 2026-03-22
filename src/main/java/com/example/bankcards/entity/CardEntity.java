package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    @Column(unique = true, nullable = false)
    private String cardNumber;
    @Column(length = 4, nullable = false)
    private String lastFourDigits;
    @Column(nullable = false)
    private String bankTitle;
    private String type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @OneToMany(mappedBy = "fromCard")
    @OnDelete(action = OnDeleteAction.CASCADE) // каскадное удаление на уровне БД
    private List<TransferEntity> outTransfers;
    @OneToMany(mappedBy = "toCard")
    @OnDelete(action = OnDeleteAction.CASCADE) // каскадное удаление на уровне БД
    private List<TransferEntity> toTransfers;

}
