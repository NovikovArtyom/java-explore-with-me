package ru.yandex.practicum.ewmmainservice.categories.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.categories.dto.CategoriesRequestDto;
import ru.yandex.practicum.ewmmainservice.categories.dto.AddCategoriesResponseDto;
import ru.yandex.practicum.ewmmainservice.mapper.CategoriesMapper;
import ru.yandex.practicum.ewmmainservice.categories.service.CategoriesService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequestMapping("/admin/categories")
public class CategoriesAdminController {
    private final CategoriesService categoriesService;
    private final CategoriesMapper categoriesMapper;

    public CategoriesAdminController(CategoriesService categoriesService, CategoriesMapper categoriesMapper) {
        this.categoriesService = categoriesService;
        this.categoriesMapper = categoriesMapper;
    }

    @PostMapping
    public ResponseEntity<AddCategoriesResponseDto> addCategories(@Valid @RequestBody CategoriesRequestDto categoriesRequestDto) {
        return ResponseEntity.ok(categoriesMapper.fromCategoriesEntityToAddCategoriesResponseDto(
                categoriesService.addCategories(
                        categoriesMapper.fromAddCategoriesRequestDtoToCategoriesEntity(categoriesRequestDto)
                )
        ));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategories(@PositiveOrZero @PathVariable Long catId) {
        categoriesService.deleteCategories(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<AddCategoriesResponseDto> patchCategories(@Valid @RequestBody CategoriesRequestDto categoriesRequestDto,
                                                                    @PositiveOrZero @PathVariable Long catId) {
        return ResponseEntity.ok(categoriesMapper.fromCategoriesEntityToAddCategoriesResponseDto(
                categoriesService.patchCategories(
                        categoriesMapper.fromAddCategoriesRequestDtoToCategoriesEntity(categoriesRequestDto), catId
                )
        ));
    }
}