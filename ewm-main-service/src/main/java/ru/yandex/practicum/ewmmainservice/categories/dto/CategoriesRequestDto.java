package ru.yandex.practicum.ewmmainservice.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoriesRequestDto {
    @NotNull
    @NotBlank
    private String name;
}
