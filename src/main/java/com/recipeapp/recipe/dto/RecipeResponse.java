package com.recipeapp.recipe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RecipeResponse {
    private Long id;
    private String title;
    private String instructions;
    private String createdBy;
    private Set<String> categoryNames;
}
