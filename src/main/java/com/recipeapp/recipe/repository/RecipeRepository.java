package com.recipeapp.recipe.repository;

import com.recipeapp.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByUserId(Long userId);
    List<Recipe> findByTitleContainingIgnoreCase(String title);

    List<Recipe> findAllByCategoryNameIgnoreCase(String categoryName);

}
