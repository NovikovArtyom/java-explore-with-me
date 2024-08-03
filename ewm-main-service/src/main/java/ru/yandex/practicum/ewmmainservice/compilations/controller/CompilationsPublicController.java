package ru.yandex.practicum.ewmmainservice.compilations.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoResponse;
import ru.yandex.practicum.ewmmainservice.compilations.service.CompilationsService;
import ru.yandex.practicum.ewmmainservice.constants.ServiceConstants;
import ru.yandex.practicum.ewmmainservice.mapper.CompilationsMapper;
import ru.yandex.practicum.statsserviceclient.client.StatsClient;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.ewmmainservice.constants.ServiceConstants.formatter;

@RestController
@RequestMapping("/compilations")
@Validated
public class CompilationsPublicController {
    private final CompilationsService compilationService;
    private final CompilationsMapper compilationsMapper;
    private final StatsClient statsClient;

    public CompilationsPublicController(CompilationsService compilationService, CompilationsMapper compilationsMapper, StatsClient statsClient) {
        this.compilationService = compilationService;
        this.compilationsMapper = compilationsMapper;
        this.statsClient = statsClient;
    }

    @GetMapping
    public ResponseEntity<List<CompilationsDtoResponse>> getAllCompilations(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, request.getRemoteAddr(), request.getRequestURI(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        return ResponseEntity.ok(
                compilationService.getAllCompilations(pinned, from, size).stream()
                        .map(compilationsMapper::fromCompilationsEntityToCompilationsDtoResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationsDtoResponse> getCompilationById(
            @Positive @PathVariable Long compId,
            HttpServletRequest request
    ) {
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, request.getRemoteAddr(), request.getRequestURI(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        return ResponseEntity.ok(
                compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationService.getCompilationById(compId))
        );
    }
}
