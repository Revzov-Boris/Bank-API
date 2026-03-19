package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private String passHash;
    private String firstName;
    private String secondName;
    private String thirdName;
    private LocalDate birthDate;
    private Role role;
}
