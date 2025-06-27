package com.recipeapp.recipe.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequest {
    private String title;
    private String instructions;
    private Set<Long> categoryIds;
}
