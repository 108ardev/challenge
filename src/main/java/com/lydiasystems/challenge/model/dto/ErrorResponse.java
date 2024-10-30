package com.lydiasystems.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ErrorResponse {

    private final String error;
    private final String message;
}
