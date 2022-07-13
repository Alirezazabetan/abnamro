package com.abnamro.recipe.service;

import com.abnamro.recipe.domain.enumeration.DishType;
import com.abnamro.recipe.domain.enumeration.Ingredient;
import com.abnamro.recipe.service.dto.RecipeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.abnamro.recipe.domain.Recipe}.
 */
public interface RecipeService {
    /**
     * Save a recipe.
     *
     * @param recipeDTO the entity to save.
     * @return the persisted entity.
     */
    RecipeDTO save(RecipeDTO recipeDTO);

    /**
     * Partially updates a recipe.
     *
     * @param recipeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecipeDTO> partialUpdate(RecipeDTO recipeDTO);


    /**
     * Get the "id" recipe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecipeDTO> findOne(Long id);

    /**
     * Delete the "id" recipe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search api on recipes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecipeDTO> search(Pageable pageable, DishType dishType, String servings, Ingredient excludeIngredient, Ingredient includeIngredient, String instruction);
}
