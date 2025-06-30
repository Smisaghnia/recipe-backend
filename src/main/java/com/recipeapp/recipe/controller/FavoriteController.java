package com.recipeapp.recipe.controller;

import com.recipeapp.recipe.dto.RecipeResponse;
import com.recipeapp.recipe.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{recipeId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long recipeId, Authentication authentication) {
        favoriteService.addFavorite(authentication.getName(), recipeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long recipeId, Authentication authentication) {
        favoriteService.removeFavorite(authentication.getName(), recipeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getFavorites(Authentication authentication) {
        List<RecipeResponse> favorites = favoriteService.getFavorites(authentication.getName());
        return ResponseEntity.ok(favorites);
    }
}
