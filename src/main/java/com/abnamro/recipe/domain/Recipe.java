package com.abnamro.recipe.domain;

import com.abnamro.recipe.domain.enumeration.DishType;
import com.abnamro.recipe.domain.enumeration.Ingredient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "instruction")
    private String instruction;

    @Column(name = "servings")
    private Integer servings;

    @Column(name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "dish_type")
    private DishType dishType;

    @ElementCollection(targetClass = Ingredient.class)
    @JoinTable(name = "tblIngredient", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "ingredient", nullable = false)
    @Enumerated(EnumType.STRING)
    Set<Ingredient> ingredients;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Recipe title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public Recipe instruction(String instruction) {
        this.setInstruction(instruction);
        return this;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Integer getServings() {
        return this.servings;
    }

    public Recipe servings(Integer servings) {
        this.setServings(servings);
        return this;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getUserId() {
        return this.userId;
    }

    public Recipe userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    public DishType getDishType() {
        return this.dishType;
    }

    public Recipe dishType(DishType dishType) {
        this.setDishType(dishType);
        return this;
    }

    public Set<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public Recipe ingredients(Set<Ingredient> ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return id != null && id.equals(((Recipe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", instruction='" + getInstruction() + "'" +
            ", servings=" + getServings() +
            ", userId='" + getUserId() + "'" +
            ", ingredients='" + getIngredients() + "'" +
            "}";
    }
}
