package ru.yandex.practicum.ewmmainservice.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsResponseDto;
import ru.yandex.practicum.ewmmainservice.comments.service.CommentsService;
import ru.yandex.practicum.ewmmainservice.mapper.CommentsMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/comments")
@Validated
@Slf4j
public class CommentsAdminController {
    private final CommentsService commentsService;
    private final CommentsMapper commentsMapper;

    public CommentsAdminController(CommentsService commentsService, CommentsMapper commentsMapper) {
        this.commentsService = commentsService;
        this.commentsMapper = commentsMapper;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentsResponseDto>> getAllCommentsByUserId(
            @Positive @PathVariable Long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                commentsService.getAllCommentsByUserId(userId, from, size).stream()
                        .map(commentsMapper::fromCommentsEntityToCommentsResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentById(
            @Positive @PathVariable Long commentId
    ) {
        commentsService.deleteCommentById(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
