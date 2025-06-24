package com.recipeapp.recipe.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}
