package ru.yandex.practicum.ewmmainservice.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoriesResponseDto {
    private Long id;
    private String name;
}
