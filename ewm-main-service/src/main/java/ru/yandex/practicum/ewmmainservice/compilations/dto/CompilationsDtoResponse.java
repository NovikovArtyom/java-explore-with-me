package ru.yandex.practicum.ewmmainservice.compilations.dto;

import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;

import java.util.List;

public class CompilationsDtoResponse {
    private List<EventResponseDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
