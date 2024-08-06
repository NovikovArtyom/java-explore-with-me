package ru.yandex.practicum.ewmmainservice.categories.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesRequestDto {
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;
}
