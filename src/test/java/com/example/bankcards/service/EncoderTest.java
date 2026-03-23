package com.example.bankcards.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class EncoderTest {
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Test
    public void demonstrationEncoderEquealsPassword() {
        String password = "123qwe";
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);
        System.out.println("hash1: " + hash1);
        System.out.println("hash2: " + hash2);

        System.out.println(passwordEncoder.matches(password, hash1));
        System.out.println(passwordEncoder.matches(password, hash1));

    }
}
