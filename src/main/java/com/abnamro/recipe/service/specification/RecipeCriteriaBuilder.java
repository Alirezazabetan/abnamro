package com.abnamro.recipe.service.specification;

import com.abnamro.recipe.domain.enumeration.DishType;
import com.abnamro.recipe.domain.enumeration.Ingredient;
import com.abnamro.recipe.service.dto.SearchCriteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeCriteriaBuilder  {

    public List<SearchCriteria> getSearchCriteria(DishType dishType, String servings, Ingredient excludeIngredient, Ingredient includeIngredient, String instruction) {
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        if (dishType != null) {
            searchCriteria.add(new SearchCriteria("dishType", ":", dishType));
        }
        if (servings != null && !servings.equals("")) {
            searchCriteria.add(new SearchCriteria("servings", ":", servings));
        }
        if (instruction != null && !instruction.equals("")) {
            searchCriteria.add(new SearchCriteria("instruction", "%", instruction));
        }
        if (includeIngredient != null) {
            searchCriteria.add(new SearchCriteria("ingredients", "in", includeIngredient));
        }
        if (excludeIngredient != null) {
            searchCriteria.add(new SearchCriteria("ingredients", "xin", excludeIngredient));
        }
        return searchCriteria;
    }
}
