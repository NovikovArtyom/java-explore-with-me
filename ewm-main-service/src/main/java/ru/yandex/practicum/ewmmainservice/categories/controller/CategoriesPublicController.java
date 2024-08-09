package ru.yandex.practicum.ewmmainservice.categories.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.categories.dto.AddCategoriesResponseDto;
import ru.yandex.practicum.ewmmainservice.constants.ServiceConstants;
import ru.yandex.practicum.ewmmainservice.categories.service.CategoriesService;
import ru.yandex.practicum.ewmmainservice.mapper.CategoriesMapper;
import ru.yandex.practicum.statsserviceclient.client.StatsClient;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.ewmmainservice.constants.ServiceConstants.formatter;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoriesPublicController {
    private final CategoriesService categoriesService;
    private final CategoriesMapper categoriesMapper;
    private final StatsClient statsClient;

    public CategoriesPublicController(CategoriesService categoriesService, CategoriesMapper categoriesMapper, StatsClient statsClient) {
        this.categoriesService = categoriesService;
        this.categoriesMapper = categoriesMapper;
        this.statsClient = statsClient;
    }

    @GetMapping
    public ResponseEntity<List<AddCategoriesResponseDto>> findAllCategories(
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, request.getRequestURI(), request.getRemoteAddr(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        return ResponseEntity.ok(categoriesService.findAllCategories(from, size).stream()
                .map(categoriesMapper::fromCategoriesEntityToAddCategoriesResponseDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<AddCategoriesResponseDto> findCategoriesById(
            @PositiveOrZero @PathVariable Long catId,
            HttpServletRequest request
    ) {
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, request.getRequestURI(), request.getRemoteAddr(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        return ResponseEntity.ok(categoriesMapper.fromCategoriesEntityToAddCategoriesResponseDto(
                categoriesService.findCategoriesById(catId)
        ));
    }
}
