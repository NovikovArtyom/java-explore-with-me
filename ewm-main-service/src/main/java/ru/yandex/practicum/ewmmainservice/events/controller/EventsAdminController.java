package ru.yandex.practicum.ewmmainservice.events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;
import ru.yandex.practicum.ewmmainservice.events.dto.PatchEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.mapper.EventsMapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
public class EventsAdminController {
    private final EventsService eventsService;
    private final EventsMapper eventsMapper;

    public EventsAdminController(EventsService eventsService, EventsMapper eventsMapper) {
        this.eventsService = eventsService;
        this.eventsMapper = eventsMapper;
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents(
            @Positive @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventsStates> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                eventsService.getAllEvents(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                        .map(eventsMapper::fromEventsEntityToEventResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> patchEventStatus(
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody PatchEventRequestDto patchEventRequestDto
            ) {
        return ResponseEntity.ok(
                eventsMapper.fromEventsEntityToEventResponseDto(eventsService.patchEventStatus(eventId, patchEventRequestDto))
        );
    }
}