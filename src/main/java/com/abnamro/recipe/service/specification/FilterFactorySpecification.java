package com.abnamro.recipe.service.specification;

import com.abnamro.recipe.domain.Recipe;
import com.abnamro.recipe.service.dto.SearchCriteria;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class FilterFactorySpecification {

    private List<SearchCriteria> searchCriteria = new ArrayList<>();

    public FilterFactorySpecification(List<SearchCriteria> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    private Predicate toPredicate(Root<? extends Object> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchCriteria criteria) {

        switch (criteria.getOperation()){
            case "in":
                return builder.equal(root.join(criteria.getKey(), JoinType.INNER), criteria.getValue());
            case "xin":
                return builder.not(builder.in(root.get("id")).value(getRecipeSubquery(query, builder, criteria)));
            case ">":
                return builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            case "<":
                return builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            case ":":
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case "%":
                return builder.like(builder.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            default:
                return null;
        }
    }

    public Predicate getSpecification(Root<? extends Object> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria s : searchCriteria) {
            predicates.add(toPredicate(root, query, builder, s));
        }
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private Subquery<Recipe> getRecipeSubquery(CriteriaQuery<?> query, CriteriaBuilder builder, SearchCriteria criteria) {
        Subquery<Recipe> subqueryTag = query.subquery(Recipe.class);
        Root<Recipe> subRootTag = subqueryTag.from(Recipe.class);
        subqueryTag.select(subRootTag.get("id"));
        Predicate predicate = builder.equal(subRootTag.join(criteria.getKey(), JoinType.INNER), criteria.getValue());
        subqueryTag.where(predicate);
        return subqueryTag;
    }
}
