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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        Set<Category> categories = new HashSet<>();
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));

            if (categories.size() != request.getCategoryIds().size()) {
                throw new IllegalArgumentException("Eine oder mehrere Kategorien nicht gefunden");
            }
        }

        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .instructions(request.getInstructions())
                .user(user)
                .categories(categories)
                .build();

        recipe = recipeRepository.save(recipe);

        return mapToResponse(recipe);
    }

    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RecipeResponse> getRecipesByCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Recipe> recipes = recipeRepository.findAllByUserId(user.getId());

        return recipes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RecipeResponse updateRecipe(Long id, RecipeRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezept nicht gefunden"));

        if (!recipe.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Kein Zugriff auf dieses Rezept");
        }

        recipe.setTitle(request.getTitle());
        recipe.setInstructions(request.getInstructions());

        Set<Category> categories = new HashSet<>();
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
            if (categories.size() != request.getCategoryIds().size()) {
                throw new IllegalArgumentException("Eine oder mehrere Kategorien nicht gefunden");
            }
        }
        recipe.setCategories(categories);

        Recipe updated = recipeRepository.save(recipe);

        return mapToResponse(updated);
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
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RecipeResponse> getRecipesByCategoryName(String categoryName) {
        List<Recipe> recipes = recipeRepository.findAllByCategories_NameIgnoreCase(categoryName);

        return recipes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RecipeResponse> getRecipesByCategoryNames(List<String> categoryNames) {
        List<Recipe> recipes = recipeRepository.findByCategoryNamesWithAllCategories(categoryNames, (long) categoryNames.size());

        return recipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .title(recipe.getTitle())
                        .instructions(recipe.getInstructions())
                        .categoryNames(recipe.getCategories().stream()
                                .map(Category::getName)
                                .collect(Collectors.toSet()))
                        .createdBy(recipe.getUser().getUsername())
                        .build())
                .collect(Collectors.toList());
    }



    // Kombinierte Suche nach Kategorie und Titel (beides optional)
    public List<RecipeResponse> searchRecipes(String categoryName, String title) {
        List<Recipe> recipes = recipeRepository.findByCategoryNameAndTitle(categoryName, title);
        return recipes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Hilfsmethode zum Mapping
    private RecipeResponse mapToResponse(Recipe recipe) {
        Set<String> categoryNames = recipe.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .instructions(recipe.getInstructions())
                .createdBy(recipe.getUser().getEmail())
                .categoryNames(categoryNames)
                .build();
    }
}
