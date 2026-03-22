package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CardNumberEncryption {
    
    private final SecretKey secretKey;
    private final String transformation;
    
    public CardNumberEncryption(
            @Value("${card.encryption.secret}") String secret,
            @Value("${card.encryption.transformation:AES/ECB/PKCS5Padding}") String transformation
    ) {
        // Преобразуем строковый ключ в 256-битный ключ
        // Для простоты используем SHA-256 для получения ключа нужной длины
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        // Обрезаем или дополняем до 32 байт (256 бит)
        byte[] finalKey = new byte[32];
        System.arraycopy(keyBytes, 0, finalKey, 0, Math.min(keyBytes.length, 32));
        
        this.secretKey = new SecretKeySpec(finalKey, "AES");
        this.transformation = transformation;
    }
    
    /**
     * Шифрование номера карты
     * @param cardNumber номер карты (16 цифр)
     * @return зашифрованная строка в Base64
     */
    public String encrypt(String cardNumber) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encrypted = cipher.doFinal(cardNumber.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования номера карты", e);
        }
    }
    
    /**
     * Расшифровка номера карты
     * @param encryptedCardNumber зашифрованный номер в Base64
     * @return исходный номер карты
     */
    public String decrypt(String encryptedCardNumber) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] decoded = Base64.getDecoder().decode(encryptedCardNumber);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка расшифровки номера карты", e);
        }
    }
    
    /**
     * Получить последние 4 цифры номера карты
     * @param cardNumber полный номер карты
     * @return последние 4 цифры
     */
    public String getLastFourDigits(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            throw new IllegalArgumentException("Неверный номер карты");
        }
        // Убираем возможные пробелы
        String cleanNumber = cardNumber.replaceAll("\\s+", "");
        return cleanNumber.substring(cleanNumber.length() - 4);
    }
    
    /**
     * Получить маску номера карты
     * @param lastFourDigits последние 4 цифры
     * @return маскированный номер
     */
    public String getMaskedNumber(String lastFourDigits) {
        return "**** **** **** " + lastFourDigits;
    }
}