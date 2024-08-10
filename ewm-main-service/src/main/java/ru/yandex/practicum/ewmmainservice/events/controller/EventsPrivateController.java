package ru.yandex.practicum.ewmmainservice.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.events.dto.AddEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;
import ru.yandex.practicum.ewmmainservice.events.dto.PatchEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.mapper.EventsMapper;
import ru.yandex.practicum.ewmmainservice.mapper.RequestMapper;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoResponse;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdateStatus;
import ru.yandex.practicum.ewmmainservice.requests.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@Slf4j
public class EventsPrivateController {
    private final EventsService eventsService;
    private final EventsMapper eventsMapper;
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    public EventsPrivateController(EventsService eventsService, EventsMapper eventsMapper, RequestService requestService,
                                   RequestMapper requestMapper) {
        this.eventsService = eventsService;
        this.eventsMapper = eventsMapper;
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> addEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody AddEventRequestDto addEventRequestDto
    ) {
        log.info("Events. Private Controller: 'addEvent' method called");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventsMapper.fromEventsEntityToEventResponseDto(eventsService.addEvent(userId,
                        eventsMapper.fromAddEventRequestDtoToEventsEntity(addEventRequestDto),
                        addEventRequestDto.getCategory())
                ));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEventsByUserId(
            @Positive @PathVariable Long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("Events. Private Controller: 'getAllEventsByUserId' method called");
        return ResponseEntity.ok(eventsService.getAllEventsByUserId(userId, from, size).stream()
                .map(eventsMapper::fromEventsEntityToEventResponseDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEventsByIdByUserId(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId
    ) {
        log.info("Events. Private Controller: 'getEventsByIdByUserId' method called");
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
        log.info("Events. Private Controller: 'patchEvent' method called");
        return ResponseEntity.ok(eventsMapper.fromEventsEntityToEventResponseDto(
                eventsService.patchEvent(userId, eventId, patchEventRequestDto)
        ));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestsDtoResponse>> getAllRequestsByEventIdByUserId(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId
    ) {
        log.info("Events. Private Controller: 'getAllRequestsByEventIdByUserId' method called");
        return ResponseEntity.ok(
                requestService.getAllRequestsByEventIdByUserId(userId, eventId).stream()
                        .map(requestMapper::fromRequestEntityToRequestDtoResponse)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<RequestsDtoUpdateStatus> updateRequestsStatus(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody RequestsDtoUpdate requestsDtoUpdate
    ) {
        log.info("Events. Private Controller: 'updateRequestsStatus' method called");
        return ResponseEntity.ok(
                requestService.updateRequestsStatus(userId, eventId, requestsDtoUpdate)
        );
    }
}
