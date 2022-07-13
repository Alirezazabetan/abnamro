package com.abnamro.recipe.service.specification;

import com.abnamro.recipe.domain.Recipe;
import com.abnamro.recipe.service.dto.SearchCriteria;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class FilterFactorySpecification {

    private List<SearchCriteria> searchCriteria = new ArrayList<>();

    public FilterFactorySpecification(List<SearchCriteria> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }


    private Predicate toPredicate(Root<? extends Object> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchCriteria criteria) {

        if (criteria.getOperation().equalsIgnoreCase("in")) {
            return builder.equal(root.join(criteria.getKey(), JoinType.INNER), criteria.getValue());
        }

        if (criteria.getOperation().equalsIgnoreCase("xin")) {
            Subquery<Recipe> subqueryTag = query.subquery(Recipe.class);
            Root<Recipe> subRootTag = subqueryTag.from(Recipe.class);
            subqueryTag.select(subRootTag.get("id"));
            Predicate predicate = builder.equal(subRootTag.join(criteria.getKey(), JoinType.INNER), criteria.getValue());
            subqueryTag.where(predicate);

            return builder.not(builder.in(root.get("id")).value(subqueryTag));
        }

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("%")) {
            return builder.like(builder.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
        }
        return null;
    }

    public Predicate getSpecification(Root<? extends Object> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria s : searchCriteria) {
            predicates.add(toPredicate(root, query, builder, s));
        }
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
