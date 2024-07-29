package ru.yandex.practicum.ewmmainservice.categories.dto;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class CategoriesRequestDto {
    @NotNull
    @NotBlank
    private String name;
}
