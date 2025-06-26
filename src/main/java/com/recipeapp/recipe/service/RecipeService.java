package com.recipeapp.recipe.service;

import com.recipeapp.recipe.dto.RecipeRequest;
import com.recipeapp.recipe.dto.RecipeResponse;
import com.recipeapp.recipe.entity.Category;
import com.recipeapp.recipe.entity.Recipe;
import com.recipeapp.recipe.entity.User;
import com.recipeapp.recipe.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public RecipeResponse createRecipe(RecipeRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Kategorie nicht gefunden"));
        }

        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .instructions(request.getInstructions())
                .user(user)
                .category(category)
                .build();

        recipe = recipeRepository.save(recipe);

        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .instructions(recipe.getInstructions())
                .createdBy(recipe.getUser().getUsername())
                .categoryName(category != null ? category.getName() : null)
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

    public RecipeResponse updateRecipe(Long id, RecipeRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezept nicht gefunden"));

        // Sicherheitsprüfung: darf nur der Ersteller bearbeiten
        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Kein Zugriff auf dieses Rezept");
        }

        // Aktualisieren
        recipe.setTitle(request.getTitle());
        recipe.setInstructions(request.getInstructions());

        Recipe updated = recipeRepository.save(recipe);

        return RecipeResponse.builder()
                .id(updated.getId())
                .title(updated.getTitle())
                .instructions(updated.getInstructions())
                .createdBy(updated.getUser().getEmail())
                .build();
    }

    public void deleteRecipe(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezept nicht gefunden"));

        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Kein Zugriff auf dieses Rezept");
        }

        recipeRepository.delete(recipe);
    }

    public List<RecipeResponse> searchRecipesByTitle(String title) {
        List<Recipe> recipes = recipeRepository.findByTitleContainingIgnoreCase(title);

        return recipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .title(recipe.getTitle())
                        .instructions(recipe.getInstructions())
                        .createdBy(recipe.getUser().getUsername())
                        .build())
                .collect(Collectors.toList());
    }
    public List<RecipeResponse> getRecipesByCategoryName(String categoryName) {
        List<Recipe> recipes = recipeRepository.findAllByCategoryNameIgnoreCase(categoryName);

        return recipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .title(recipe.getTitle())
                        .instructions(recipe.getInstructions())
                        .categoryName(recipe.getCategory().getName())
                        .createdBy(recipe.getUser().getUsername())
                        .build())
                .collect(Collectors.toList());
    }

}
