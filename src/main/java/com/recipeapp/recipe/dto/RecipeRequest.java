package com.recipeapp.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequest {
    private String title;
    private String instructions;
    private Long categoryId;
}
