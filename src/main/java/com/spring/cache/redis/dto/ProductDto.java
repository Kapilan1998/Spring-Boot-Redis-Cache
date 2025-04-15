package com.spring.cache.redis.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

//record is a succinct way to create immutable data objects.
//It's especially useful for things like DTOs or configurations where we don’t want to manually write constructors, getters, and boilerplate.
public record ProductDto(Long id, @NotBlank String name, @Positive BigDecimal price) {
}