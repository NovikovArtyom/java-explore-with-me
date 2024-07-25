package ru.yandex.practicum.statsserviceserver.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;
import ru.yandex.practicum.statsservicedto.HitDtoResponse;
import ru.yandex.practicum.statsservicedto.StatsDtoResponse;
import ru.yandex.practicum.statsserviceserver.mapper.StatsMapper;
import ru.yandex.practicum.statsserviceserver.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Validated

public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<HitDtoResponse> addHit(@Valid @RequestBody HitDtoRequest hitDtoRequest) {
        return ResponseEntity.ok(StatsMapper.INSTANCE.hitEntityToHitDtoResponse
                (statsService.addHit(StatsMapper.INSTANCE.hitDtoRequestToHitEntity(hitDtoRequest))));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDtoResponse>> getStats(@RequestParam(required = true)String start,
                                                           @RequestParam(required = true)String end,
                                                           @RequestParam(required = false)List<String> uris,
                                                           @RequestParam(required = false, defaultValue = "false")Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTimeStart = LocalDateTime.parse(start, formatter);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(end, formatter);
        return ResponseEntity.ok(statsService.getStats(localDateTimeStart, localDateTimeEnd, uris, unique).stream()
                .map(StatsMapper.INSTANCE::statsViewToStatsDtoResponse)
                .collect(Collectors.toList()));
    }
}
