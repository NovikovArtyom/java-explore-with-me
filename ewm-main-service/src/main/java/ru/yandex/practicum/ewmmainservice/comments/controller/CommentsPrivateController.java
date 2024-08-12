package ru.yandex.practicum.ewmmainservice.comments.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsRequestDto;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsResponseDto;
import ru.yandex.practicum.ewmmainservice.comments.service.CommentsService;
import ru.yandex.practicum.ewmmainservice.mapper.CommentsMapper;


@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
@Slf4j
public class CommentsPrivateController {
    private final CommentsService commentsService;
    private final CommentsMapper commentsMapper;

    public CommentsPrivateController(CommentsService commentsService, CommentsMapper commentsMapper) {
        this.commentsService = commentsService;
        this.commentsMapper = commentsMapper;
    }

    @PostMapping("/events/{eventId}")
    public ResponseEntity<CommentsResponseDto> createComment(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody CommentsRequestDto commentsRequestDto
    ) {
        log.info("Comments. Private Controller: 'createComment' method called");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commentsMapper.fromCommentsEntityToCommentsResponseDto(
                        commentsService.createComment(
                                userId, eventId, commentsMapper.fromCommentsRequestDtoToCommentsEntity(commentsRequestDto)
                        )
                )
        );
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentsResponseDto> updateComment(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long commentId,
            @Valid @RequestBody CommentsRequestDto commentsRequestDto
    ) {
        log.info("Comments. Private Controller: 'updateComment' method called");
        return ResponseEntity.ok(
                commentsMapper.fromCommentsEntityToCommentsResponseDto(
                        commentsService.updateComment(userId, commentId, commentsRequestDto)
                )
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentByOwner(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long commentId
    ) {
        log.info("Comments. Private Controller: 'deleteCommentByOwner' method called");
        commentsService.deleteCommentByOwner(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
