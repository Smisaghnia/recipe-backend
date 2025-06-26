package com.recipeapp.recipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RecipeApplication {
    public static void main(String[] args) {

        SpringApplication.run(RecipeApplication.class, args);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin";
        String hashedPassword = "$2a$10$O14DpARRVlD4un9HN/NwVegP7Jnqf4IcGxjRqWy8X9tYnaIyBRBjm";

        System.out.println("Matches? " + encoder.matches(rawPassword, hashedPassword));}
}