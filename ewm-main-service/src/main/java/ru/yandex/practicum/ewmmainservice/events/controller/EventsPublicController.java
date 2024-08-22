package ru.yandex.practicum.ewmmainservice.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.util.ServiceConstants;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.mapper.EventsMapper;
import ru.yandex.practicum.statsserviceclient.client.StatsClient;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.ewmmainservice.util.ServiceConstants.formatter;

@RestController
@RequestMapping("/events")
@Validated
@Slf4j
public class EventsPublicController {
    private final EventsService eventsService;
    private final EventsMapper eventsMapper;
    private final StatsClient statsClient;

    public EventsPublicController(EventsService eventsService, EventsMapper eventsMapper, StatsClient statsClient) {
        this.eventsService = eventsService;
        this.eventsMapper = eventsMapper;
        this.statsClient = statsClient;
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
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Events. Public Controller: 'getAllEventsWithFiltration' method called");
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, request.getRequestURI(), request.getRemoteAddr(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        return ResponseEntity.ok(
                eventsService.getAllEventsWithFiltration(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                                sort, from, size).stream()
                        .map(eventsMapper::fromEventsEntityToEventResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventsById(
            @Positive @PathVariable Long id,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {
        log.info("Events. Public Controller: 'getEventsById' method called");
        String requestURI = request.getRequestURI();
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, requestURI, request.getRemoteAddr(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        ResponseEntity<Object> response = statsClient.getViews(requestURI);
        Integer views = null;
        if (response.getBody() instanceof Integer) {
            views = (Integer) response.getBody();
        } else if (response.getBody() instanceof Long) {
            views = ((Long) response.getBody()).intValue();
        }
        return ResponseEntity.ok(
                eventsMapper.fromEventsEntityToEventResponseDto(eventsService.getEventsById(id, views))
        );
    }
}
