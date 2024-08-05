package ru.yandex.practicum.ewmmainservice.categories.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesRequestDto {
    @NotNull
    @NotBlank
    private String name;
}
