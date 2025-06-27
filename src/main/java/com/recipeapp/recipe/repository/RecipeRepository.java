package com.recipeapp.recipe.repository;

import com.recipeapp.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByUserId(Long userId);
    List<Recipe> findByTitleContainingIgnoreCase(String title);

    // Suche alle Rezepte mit Kategorie-Name (Many-to-Many)
    List<Recipe> findAllByCategories_NameIgnoreCase(String categoryName);

    // Kombinierte Suche (JPQL)
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.categories c " +
            "WHERE (:categoryName IS NULL OR LOWER(c.name) = LOWER(:categoryName)) " +
            "AND (:title IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    List<Recipe> findByCategoryNameAndTitle(@Param("categoryName") String categoryName,
                                            @Param("title") String title);

    @Query("""
    SELECT r FROM Recipe r JOIN r.categories c
    WHERE c.name IN :categoryNames
    GROUP BY r.id
    HAVING COUNT(DISTINCT c.name) = :categoryCount
""")
    List<Recipe> findByCategoryNamesWithAllCategories(
            @Param("categoryNames") List<String> categoryNames,
            @Param("categoryCount") Long categoryCount
    );


}
