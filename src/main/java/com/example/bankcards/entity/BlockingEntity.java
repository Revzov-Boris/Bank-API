package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;

@Entity
@Table(name = "blockings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CardEntity card;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private BlockingStatus status;
}
