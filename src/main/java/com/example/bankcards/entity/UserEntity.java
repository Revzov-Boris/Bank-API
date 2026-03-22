package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false, length = 60)
    private String login;
    // хэш пароля
    @Column(nullable = false)
    private String passHash;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String secondName;
    private String thirdName;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE) // каскадное удаление на уровне БД
    private List<CardEntity> cards;
}
