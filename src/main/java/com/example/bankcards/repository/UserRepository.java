package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role")
    Page<UserEntity> findAllByRole(Pageable pageable, @Param("role") Role role);
    Optional<UserEntity> findByLogin(String login);
    @Query("SELECT u.id FROM UserEntity u WHERE u.login = :login")
    Integer findIdByLogin(@Param("login") String login);
}
