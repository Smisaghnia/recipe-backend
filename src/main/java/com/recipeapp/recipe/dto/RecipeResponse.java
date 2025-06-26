package com.recipeapp.recipe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeResponse {
    private Long id;
    private String title;
    private String instructions;
    private String createdBy;
    private String categoryName;
}
