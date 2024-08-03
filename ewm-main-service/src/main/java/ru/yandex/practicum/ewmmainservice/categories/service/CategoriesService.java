package ru.yandex.practicum.ewmmainservice.categories.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;

public interface CategoriesService {
    CategoriesEntity addCategories(CategoriesEntity categoriesEntity);

    void deleteCategories(Long catId);

    CategoriesEntity patchCategories(CategoriesEntity categoriesEntity, Long catId);

    Page<CategoriesEntity> findAllCategories(Integer from, Integer size);

    CategoriesEntity findCategoriesById(Long catId);
}