package com.recipeapp.recipe.service;

import com.recipeapp.recipe.dto.RecipeResponse;
import com.recipeapp.recipe.entity.Recipe;
import com.recipeapp.recipe.entity.User;
import com.recipeapp.recipe.repository.RecipeRepository;
import com.recipeapp.recipe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.recipeapp.recipe.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public void addFavorite(String email, Long recipeId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);
    }

    public void removeFavorite(String email, Long recipeId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        user.getFavoriteRecipes().remove(recipe);
        userRepository.save(user);
    }

    public List<RecipeResponse> getFavorites(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getFavoriteRecipes().stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .title(recipe.getTitle())
                        .instructions(recipe.getInstructions())
                        .createdBy(recipe.getUser().getEmail())  // falls createdBy email sein soll
                        .categoryNames(recipe.getCategories().stream()
                                .map(Category::getName)
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }

}
