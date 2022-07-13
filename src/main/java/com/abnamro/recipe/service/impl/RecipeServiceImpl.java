package com.abnamro.recipe.service.impl;

import com.abnamro.recipe.domain.Recipe;
import com.abnamro.recipe.domain.enumeration.DishType;
import com.abnamro.recipe.domain.enumeration.Ingredient;
import com.abnamro.recipe.repository.RecipeRepository;
import com.abnamro.recipe.service.RecipeService;
import com.abnamro.recipe.service.dto.RecipeDTO;
import com.abnamro.recipe.service.dto.SearchCriteria;
import com.abnamro.recipe.service.mapper.RecipeMapper;
import com.abnamro.recipe.service.specification.FilterFactorySpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Recipe}.
 */
@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public RecipeDTO save(RecipeDTO recipeDTO) {
        log.debug("Request to save Recipe : {}", recipeDTO);
        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe);
    }

    @Override
    public Optional<RecipeDTO> partialUpdate(RecipeDTO recipeDTO) {
        log.debug("Request to partially update Recipe : {}", recipeDTO);

        return recipeRepository
                .findById(recipeDTO.getId())
                .map(existingRecipe -> {
                    recipeMapper.partialUpdate(existingRecipe, recipeDTO);

                    return existingRecipe;
                })
                .map(recipeRepository::save)
                .map(recipeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeDTO> findOne(Long id) {
        log.debug("Request to get Recipe : {}", id);
        return recipeRepository.findById(id).map(recipeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Recipe : {}", id);
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDTO> search(Pageable pageable, DishType dishType, String servings, Ingredient excludeIngredient, Ingredient includeIngredient, String instruction) {
        log.debug("Request to search on Recipe with params : dishType {}, servings {}, excludeIngredient {}, includeIngredient {}, instruction {}", dishType, servings, excludeIngredient, includeIngredient, instruction);
        FilterFactorySpecification contentSpecification = new FilterFactorySpecification(getSearchCriteria(dishType, servings, excludeIngredient, includeIngredient, instruction));
        Page<Recipe> resultList = recipeRepository.findAll((root, query, criteriaBuilder) -> {
            return contentSpecification.getSpecification(root, query, criteriaBuilder);
        }, pageable);

        List<RecipeDTO> collect = resultList.stream().map(recipeMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, resultList.getTotalElements());
    }

    private List<SearchCriteria> getSearchCriteria(DishType dishType, String servings, Ingredient excludeIngredient, Ingredient includeIngredient, String instruction) {
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
