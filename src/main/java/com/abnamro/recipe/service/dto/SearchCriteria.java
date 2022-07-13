package com.abnamro.recipe.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}