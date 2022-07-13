package com.abnamro.recipe.controller;

import com.abnamro.recipe.controller.errors.BadRequestAlertException;
import com.abnamro.recipe.controller.util.HeaderUtil;
import com.abnamro.recipe.controller.util.PaginationUtil;
import com.abnamro.recipe.controller.util.ResponseUtil;
import com.abnamro.recipe.domain.Recipe;
import com.abnamro.recipe.domain.enumeration.DishType;
import com.abnamro.recipe.domain.enumeration.Ingredient;
import com.abnamro.recipe.repository.RecipeRepository;
import com.abnamro.recipe.service.RecipeService;
import com.abnamro.recipe.service.dto.RecipeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Recipe}.
 */
@RestController
@RequestMapping("/api")
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "recipe";

    private String applicationName;

    private final RecipeService recipeService;

    private final RecipeRepository recipeRepository;

    public RecipeResource(RecipeService recipeService, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
    }

    /**
     * {@code GET  /recipes} : get all the recipes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
     */

    @Operation(summary = "Get recipes list with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content)})
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> searchRecipes(Pageable pageable,
               @RequestParam(value = "dishType", required = false) DishType dishType,
               @RequestParam(value = "servings", required = false) String servings,
               @RequestParam(value = "excludeIngredient", required = false) Ingredient excludeIngredient,
               @RequestParam(value = "includeIngredient", required = false) Ingredient includeIngredient,
               @RequestParam(value = "instruction", required = false) String instruction) {
        log.debug("REST request to get a page of Recipes");

        Page<RecipeDTO> page = recipeService.search(pageable,dishType,servings,excludeIngredient,includeIngredient,instruction);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code POST  /recipes} : Create a new recipe.
     *
     * @param recipeDTO the recipeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipeDTO, or with status
     * {@code 400 (Bad Request)} if the recipe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @Operation(summary = "Add New Recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Recipe has already an ID",
                    content = @Content)})
    @PostMapping("/recipes")
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeDTO);
        if (recipeDTO.getId() != null) {
            throw new BadRequestAlertException("A new recipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeDTO result = recipeService.save(recipeDTO);
        return ResponseEntity
                .created(new URI(
                        "/api/recipes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /recipes/:id} : get the "id" recipe.
     *
     * @param id the id of the recipeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipeDTO, or with status {@code 404 (Not Found)}.
     */
    @Operation(summary = "Get recipe with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Recipe Not Found",
                    content = @Content)})
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        Optional<RecipeDTO> recipeDTO = recipeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeDTO);
    }

    /**
     * {@code PATCH  /recipes/:id} : Partial updates given fields of an existing recipe, field will ignore if it is null
     *
     * @param id        the id of the recipeDTO to save.
     * @param recipeDTO the recipeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recipeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @Operation(summary = "Update partially the recipe with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "RecipeDTO is not valid",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Recipe Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "RecipeDTO couldn't be updated",
                    content = @Content)})
    @PatchMapping(value = "/recipes/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<RecipeDTO> partialUpdateRecipe(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody RecipeDTO recipeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recipe partially : {}, {}", id, recipeDTO);
        if (recipeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecipeDTO> result = recipeService.partialUpdate(recipeDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recipeDTO.getId().toString())
        );
    }

    /**
     * {@code PUT  /recipts/:id} : Updates an existing recipt.
     *
     * @param id the id of the reciptDTO to save.
     * @param reciptDTO the reciptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reciptDTO,
     * or with status {@code 400 (Bad Request)} if the reciptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reciptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> updateRecipt(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody RecipeDTO reciptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Recipt : {}, {}", id, reciptDTO);
        if (reciptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reciptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecipeDTO result = recipeService.save(reciptDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reciptDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code DELETE  /recipes/:id} : delete the "id" recipe.
     *
     * @param id the id of the recipeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(summary = "Delete the recipe with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "204", description = "NO_CONTENT",
                    content = @Content)})
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
