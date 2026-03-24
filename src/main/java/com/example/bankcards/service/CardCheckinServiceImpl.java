package com.example.bankcards.service;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class CardCheckinServiceImpl implements CardCheckingService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardCheckinServiceImpl(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public boolean hasUserCard(int userId, int cardId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        return userEntity.getCards().stream()
                .filter(c -> c.getId().equals(cardId))
                .findFirst().isPresent();
    }

    @Override
    @Transactional
    public void isCardsValidForTransers(CardEntity from, CardEntity to, BigDecimal money, int userId) {
        if (!from.getStatus().equals(CardStatus.ACTIVE) || !to.getStatus().equals(CardStatus.ACTIVE)) {
            throw new TransferException("Перевод возможен только между активными картами");
        }
        // если не достаточно денег на карте-отправителе
        if (from.getBalance().compareTo(money) == -1) {
            throw new TransferException("На карте-отправителе не достаточно средств");
        }
        // если пользователь не владеет обоими картами
        if (!hasUserCard(userId, from.getId()) || !hasUserCard(userId, to.getId())) {
            throw new TransferException("Можно делать переводы только своими картами");
        }
    }
}
