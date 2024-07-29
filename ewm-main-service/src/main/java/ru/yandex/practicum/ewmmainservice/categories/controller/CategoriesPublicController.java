package ru.yandex.practicum.ewmmainservice.categories.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.categories.dto.AddCategoriesResponseDto;
import ru.yandex.practicum.ewmmainservice.mapper.CategoriesMapper;
import ru.yandex.practicum.ewmmainservice.categories.service.CategoriesService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoriesPublicController {
    private final CategoriesService categoriesService;
    private final CategoriesMapper categoriesMapper;

    public CategoriesPublicController(CategoriesService categoriesService, CategoriesMapper categoriesMapper) {
        this.categoriesService = categoriesService;
        this.categoriesMapper = categoriesMapper;
    }

    @GetMapping
    public ResponseEntity<List<AddCategoriesResponseDto>> findAllCategories(
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(categoriesService.findAllCategories(from, size).stream()
                .map(categoriesMapper::fromCategoriesEntityToAddCategoriesResponseDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<AddCategoriesResponseDto> findCategoriesById(@PositiveOrZero @PathVariable Long catId) {
        return ResponseEntity.ok(categoriesMapper.fromCategoriesEntityToAddCategoriesResponseDto(
                categoriesService.findCategoriesById(catId)
        ));
    }
}
