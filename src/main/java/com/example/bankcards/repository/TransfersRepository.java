package com.example.bankcards.repository;

import com.example.bankcards.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransfersRepository extends JpaRepository<TransferEntity, Long> {
}
