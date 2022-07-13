package com.abnamro.recipe.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ErrorDTO {

    private String code;

    private String message;
}
