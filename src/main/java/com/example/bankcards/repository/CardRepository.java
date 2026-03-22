package com.example.bankcards.repository;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Integer> {
    Page<CardEntity> findAll(Pageable pageable);
    Page<CardEntity> findByStatus(Pageable pageable, CardStatus status);

    @Query("SELECT c FROM CardEntity c WHERE c.balance BETWEEN :minBalance AND :maxBalance AND c.status = :status")
    Page<CardEntity> findByBalanceBetweenAndStatus(
            @Param("minBalance") BigDecimal minBalance,
            @Param("maxBalance") BigDecimal maxBalance,
            @Param("status") CardStatus status,
            Pageable pageable);

    @Query("SELECT c FROM CardEntity c WHERE c.balance BETWEEN :minBalance AND :maxBalance")
    Page<CardEntity> findByBalanceBetween(
            Pageable pageable,
            @Param("minBalance") BigDecimal minBalance,
            @Param("maxBalance") BigDecimal maxBalance
    );

    Optional<CardEntity> findByCardNumber(String cardNumber);



}

