package ru.yandex.practicum.ewmmainservice.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
