package ru.yandex.practicum.ewmmainservice.comments.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsRequestDto;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsResponseDto;
import ru.yandex.practicum.ewmmainservice.comments.model.CommentsEntity;

import java.util.List;

public interface CommentsService {
    Page<CommentsEntity> getAllCommentsByUserId(Long userId, Integer from, Integer size);

    void deleteCommentById(Long commentId);

    CommentsEntity createComment(Long userId, Long eventId, CommentsEntity commentsEntity);

    CommentsEntity updateComment(Long userId, Long commentId, CommentsRequestDto commentsRequestDto);

    void deleteCommentByOwner(Long userId, Long commentId);

    CommentsEntity getCommentById(Long commentId);

    Page<CommentsEntity> getAllCommentsByEventId(Long eventId, Integer from, Integer size);
}
