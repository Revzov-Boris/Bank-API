package com.example.bankcards.service;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardCheckinServiceImpl implements CardCheckingService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private static final BigDecimal MAX_BALANСE = new BigDecimal("9999999999.99");

    @Autowired
    public CardCheckinServiceImpl(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }


    @Override
    public boolean hasUserCard(int userId, int cardId) {
        return cardRepository.existsByUserIdAndId(userId, cardId);
    }

    @Override
    public void isCardsValidForTransers(CardEntity from, CardEntity to, BigDecimal money, int userId) {
        if (!from.getStatus().equals(CardStatus.ACTIVE) || !to.getStatus().equals(CardStatus.ACTIVE)) {
            throw new TransferException("Перевод возможен только между активными картами");
        }
        // если не достаточно денег на карте-отправителе
        if (from.getBalance().compareTo(money) < 0) {
            throw new TransferException("На карте-отправителе не достаточно средств");
        }
        //если после перевода на карте-получателе сумма станет больше максимально допустимой
        if (to.getBalance().add(money).compareTo(MAX_BALANСE) > 0) {
            throw new TransferException("На карте получателя баланс будет превышен");
        }
        // если пользователь не владеет обоими картами
        if (!hasUserCard(userId, from.getId()) || !hasUserCard(userId, to.getId())) {
            throw new TransferException("Можно делать переводы только своими картами");
        }
    }
}
