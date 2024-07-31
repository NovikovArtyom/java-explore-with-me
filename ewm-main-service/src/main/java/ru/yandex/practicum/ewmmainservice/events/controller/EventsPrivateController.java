package ru.yandex.practicum.ewmmainservice.events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.events.dto.AddEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;
import ru.yandex.practicum.ewmmainservice.events.dto.PatchEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.mapper.EventsMapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class EventsPrivateController {
    private final EventsService eventsService;
    private final EventsMapper eventsMapper;

    public EventsPrivateController(EventsService eventsService, EventsMapper eventsMapper) {
        this.eventsService = eventsService;
        this.eventsMapper = eventsMapper;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> addEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody AddEventRequestDto addEventRequestDto
    ) {
        return ResponseEntity.ok(eventsMapper.fromEventsEntityToEventResponseDto(
                eventsService.addEvent(userId, eventsMapper.fromAddEventRequestDtoToEventsEntity(addEventRequestDto))
        ));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEventsByUserId(
            @Positive @PathVariable Long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(eventsService.getAllEventsByUserId(userId, from, size).stream()
                .map(eventsMapper::fromEventsEntityToEventResponseDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEventsByIdByUserId(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(eventsMapper.fromEventsEntityToEventResponseDto(
                eventsService.getEventsByIdByUserId(userId, eventId)
        ));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> patchEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody PatchEventRequestDto patchEventRequestDto
    ) {
        return ResponseEntity.ok(eventsMapper.fromEventsEntityToEventResponseDto(
                eventsService.patchEvent(userId, eventId, patchEventRequestDto)
        ));
    }
}
