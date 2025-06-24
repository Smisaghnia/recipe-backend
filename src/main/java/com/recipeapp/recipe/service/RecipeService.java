package com.recipeapp.recipe.service;

import com.recipeapp.recipe.dto.RecipeRequest;
import com.recipeapp.recipe.dto.RecipeResponse;
import com.recipeapp.recipe.entity.Recipe;
import com.recipeapp.recipe.entity.User;
import com.recipeapp.recipe.repository.RecipeRepository;
import com.recipeapp.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeResponse createRecipe(RecipeRequest request) {
        // Authentifizierten Benutzer ermitteln
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .instructions(request.getInstructions())
                .user(user)
                .build();

        Recipe saved = recipeRepository.save(recipe);

        return RecipeResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .instructions(saved.getInstructions())
                .createdBy(user.getEmail())
                .build();
    }
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .title(recipe.getTitle())
                        .instructions(recipe.getInstructions())
                        .createdBy(recipe.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public List<RecipeResponse> getRecipesByCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Recipe> recipes = recipeRepository.findAllByUserId(user.getId());

        // Manuelles Mapping Entity → DTO
        return recipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .title(recipe.getTitle())
                        .instructions(recipe.getInstructions())
                        .createdBy(recipe.getUser().getUsername()) // oder getEmail(), je nach Wunsch
                        .build())
                .collect(Collectors.toList());

    }

}
