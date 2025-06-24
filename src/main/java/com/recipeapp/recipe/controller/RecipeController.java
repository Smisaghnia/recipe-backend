package com.recipeapp.recipe.controller;

import com.recipeapp.recipe.dto.RecipeRequest;
import com.recipeapp.recipe.dto.RecipeResponse;
import com.recipeapp.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody RecipeRequest request) {
        return ResponseEntity.ok(recipeService.createRecipe(request));
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/my-recipes")
    public ResponseEntity<List<RecipeResponse>> getMyRecipes() {
        return ResponseEntity.ok(recipeService.getRecipesByCurrentUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeRequest request
    ) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }



}
