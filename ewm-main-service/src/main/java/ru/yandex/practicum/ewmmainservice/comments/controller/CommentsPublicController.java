package ru.yandex.practicum.ewmmainservice.comments.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsResponseDto;
import ru.yandex.practicum.ewmmainservice.comments.service.CommentsService;
import ru.yandex.practicum.ewmmainservice.mapper.CommentsMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
public class CommentsPublicController {
    private final CommentsService commentsService;
    private final CommentsMapper commentsMapper;

    public CommentsPublicController(CommentsService commentsService, CommentsMapper commentsMapper) {
        this.commentsService = commentsService;
        this.commentsMapper = commentsMapper;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentsResponseDto> getCommentById(
            @Positive @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(
                commentsMapper.fromCommentsEntityToCommentsResponseDto(commentsService.getCommentById(commentId))
        );
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CommentsResponseDto>> getAllCommentsByEventId(
            @Positive @PathVariable Long eventId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                commentsService.getAllCommentsByEventId(eventId, from, size).stream()
                        .map(commentsMapper::fromCommentsEntityToCommentsResponseDto)
                        .collect(Collectors.toList())
        );
    }
}
