package com.abnamro.recipe.service.dto;

import com.abnamro.recipe.domain.enumeration.DishType;
import com.abnamro.recipe.domain.enumeration.Ingredient;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.abnamro.recipe.domain.Recipe} entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RecipeDTO implements Serializable {

    private Long id;

    private String title;

    private String instruction;

    private Integer servings;

    private String userId;

    private DishType dishType;

    private Set<Ingredient> ingredients;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
