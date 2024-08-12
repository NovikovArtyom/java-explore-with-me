package ru.yandex.practicum.ewmmainservice.comments.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsResponseDto;
import ru.yandex.practicum.ewmmainservice.comments.service.CommentsService;
import ru.yandex.practicum.ewmmainservice.mapper.CommentsMapper;
import ru.yandex.practicum.ewmmainservice.util.ServiceConstants;
import ru.yandex.practicum.statsserviceclient.client.StatsClient;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.ewmmainservice.util.ServiceConstants.formatter;

@RestController
@Validated
@RequestMapping("/comments")
@Slf4j
public class CommentsPublicController {
    private final CommentsService commentsService;
    private final CommentsMapper commentsMapper;
    private final StatsClient statsClient;

    public CommentsPublicController(CommentsService commentsService, CommentsMapper commentsMapper, StatsClient statsClient) {
        this.commentsService = commentsService;
        this.commentsMapper = commentsMapper;
        this.statsClient = statsClient;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentsResponseDto> getCommentById(
            @Positive @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        log.info("Comments. Public Controller: 'getCommentById' method called");
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, request.getRequestURI(), request.getRemoteAddr(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        return ResponseEntity.ok(
                commentsMapper.fromCommentsEntityToCommentsResponseDto(commentsService.getCommentById(commentId))
        );
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CommentsResponseDto>> getAllCommentsByEventId(
            @Positive @PathVariable Long eventId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Comments. Public Controller: 'getAllCommentsByEventId' method called");
        statsClient.addHit(new HitDtoRequest(ServiceConstants.server, request.getRequestURI(), request.getRemoteAddr(),
                URLEncoder.encode(LocalDateTime.now().format(formatter), StandardCharsets.UTF_8)));
        return ResponseEntity.ok(
                commentsService.getAllCommentsByEventId(eventId, from, size).stream()
                        .map(commentsMapper::fromCommentsEntityToCommentsResponseDto)
                        .collect(Collectors.toList())
        );
    }
}
