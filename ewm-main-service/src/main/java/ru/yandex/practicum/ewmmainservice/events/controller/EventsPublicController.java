package ru.yandex.practicum.ewmmainservice.events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.mapper.EventsMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@Validated
public class EventsPublicController {
    private final EventsService eventsService;
    private final EventsMapper eventsMapper;

    public EventsPublicController(EventsService eventsService, EventsMapper eventsMapper) {
        this.eventsService = eventsService;
        this.eventsMapper = eventsMapper;
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEventsWithFiltration(
            @RequestParam(required = false, defaultValue = "") String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                eventsService.getAllEventsWithFiltration(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size).stream()
                .map(eventsMapper::fromEventsEntityToEventResponseDto)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventsById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(
                eventsMapper.fromEventsEntityToEventResponseDto(eventsService.getEventsById(id))
        );
    }
}
