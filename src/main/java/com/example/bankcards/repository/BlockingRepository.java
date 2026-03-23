package com.example.bankcards.repository;

import com.example.bankcards.entity.BlockingEntity;
import com.example.bankcards.entity.BlockingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockingRepository extends JpaRepository<BlockingEntity, Long> {
    @Query("SELECT COUNT(b) FROM BlockingEntity b WHERE b.card.id = :cardId AND b.status = :status")
    int findCountByCardAndStatus(@Param("cardId") int cardId,
                                 @Param("status") BlockingStatus status);
}
