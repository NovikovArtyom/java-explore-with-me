package ru.yandex.practicum.statsserviceserver.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;
import ru.yandex.practicum.statsservicedto.HitDtoResponse;
import ru.yandex.practicum.statsservicedto.StatsDtoResponse;
import ru.yandex.practicum.statsserviceserver.mapper.StatsMapper;
import ru.yandex.practicum.statsserviceserver.service.StatsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Validated
@Slf4j
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<HitDtoResponse> addHit(@Valid @RequestBody HitDtoRequest hitDtoRequest) {
        log.info("Запрос попал в метод контроллера - addHit");
        return ResponseEntity.ok(StatsMapper.hitEntityToHitDtoResponse(statsService.addHit(StatsMapper.hitDtoRequestToHitEntity(hitDtoRequest))));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDtoResponse>> getStats(@RequestParam(required = true) String start,
                                                           @RequestParam(required = true) String end,
                                                           @RequestParam(required = false) List<String> uris,
                                                           @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Запрос попал в метод контроллера - getStats");
        return ResponseEntity.ok(statsService.getStats(StatsMapper.asLocalDateTime(start), StatsMapper.asLocalDateTime(end), uris, unique).stream()
                .map(StatsMapper::statsViewToStatsDtoResponse)
                .collect(Collectors.toList()));
    }
}
