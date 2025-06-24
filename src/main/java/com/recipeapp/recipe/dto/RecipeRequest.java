package com.recipeapp.recipe.dto;

import lombok.Data;

@Data
public class RecipeRequest {
    private String title;
    private String instructions;
}
