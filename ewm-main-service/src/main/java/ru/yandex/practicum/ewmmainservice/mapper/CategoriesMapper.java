package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.ewmmainservice.categories.dto.AddCategoriesResponseDto;
import ru.yandex.practicum.ewmmainservice.categories.dto.CategoriesRequestDto;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {
    CategoriesEntity fromAddCategoriesRequestDtoToCategoriesEntity(CategoriesRequestDto categoriesRequestDto);

    AddCategoriesResponseDto fromCategoriesEntityToAddCategoriesResponseDto(CategoriesEntity categoriesEntity);
}
