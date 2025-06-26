package com.recipeapp.recipe;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordCheckTest {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "admin";
        String hashedPassword = "$2a$10$cAWHK/789s3ggGnKFQboTuz6g2RVxMlEMqsBTfuQbRAlc2n.p36sm";

        boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);
        System.out.println("Passwort passt zum Hash? " + matches);
    }
}
