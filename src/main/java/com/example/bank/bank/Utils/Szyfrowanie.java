package com.example.bank.bank.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Szyfrowanie {

    @Bean
    public static String zaszyfrujHaslo(String text) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(text);

        return hashedPassword;
    }

    @Autowired
    public static boolean sprawdzPoporawnoscHasla(String providedPassword, String hashedPasswordFromDatabase) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.matches(providedPassword, hashedPasswordFromDatabase);
    }
}
