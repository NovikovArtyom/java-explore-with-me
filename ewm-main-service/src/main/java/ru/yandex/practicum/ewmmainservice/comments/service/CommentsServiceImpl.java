package ru.yandex.practicum.ewmmainservice.comments.service;

import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsRequestDto;
import ru.yandex.practicum.ewmmainservice.comments.model.CommentsEntity;
import ru.yandex.practicum.ewmmainservice.comments.repository.CommentsRepository;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.exception.CommentNotFoundException;
import ru.yandex.practicum.ewmmainservice.exception.DataIntegrityViolationException;
import ru.yandex.practicum.ewmmainservice.exception.IncorrectEventStateException;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
import ru.yandex.practicum.ewmmainservice.user.service.UserService;

import java.time.LocalDateTime;

@Service
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserService userService;
    private final EventsService eventsService;

    public CommentsServiceImpl(CommentsRepository commentsRepository, UserService userService, EventsService eventsService) {
        this.commentsRepository = commentsRepository;
        this.userService = userService;
        this.eventsService = eventsService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentsEntity> getAllCommentsByUserId(Long userId, Integer from, Integer size) {
        UserEntity user = userService.findUserById(userId);
        return commentsRepository.findAllByUser_Id(user.getId(), PageRequest.of(from, size));
    }

    @Override
    @Transactional
    public void deleteCommentById(Long commentId) {
        if (commentsRepository.existsById(commentId)) {
            commentsRepository.deleteById(commentId);
        } else {
            throw new CommentNotFoundException(commentId);
        }
    }

    @Override
    @Transactional
    public CommentsEntity createComment(Long userId, Long eventId, CommentsEntity commentsEntity) {
        UserEntity user = userService.findUserById(userId);
        EventsEntity event = eventsService.findEventById(eventId);
        if (event.getStates().equals(EventsStates.PUBLISHED)) {
            commentsEntity.setPublished(LocalDateTime.now());
            commentsEntity.setUser(user);
            commentsEntity.setEvent(event);
            return commentsRepository.save(commentsEntity);
        } else {
            throw new IncorrectEventStateException();
        }
    }

    @Override
    @Transactional
    public CommentsEntity updateComment(Long userId, Long commentId, CommentsRequestDto commentsRequestDto) {
        UserEntity user = userService.findUserById(userId);
        CommentsEntity comment = commentsRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if (comment.getUser().equals(user)) {
            comment.setText(commentsRequestDto.getText());
            return commentsRepository.save(comment);
        } else {
            throw new DataIntegrityViolationException("Редактируемый комметарий оставлен другим пользователем");
        }
    }

    @Override
    @Transactional
    public void deleteCommentByOwner(Long userId, Long commentId) {
        UserEntity user = userService.findUserById(userId);
        CommentsEntity comment = commentsRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if (comment.getUser().equals(user)) {
            commentsRepository.deleteById(commentId);
        } else {
            throw new DataIntegrityViolationException("Удаляемый комметарий оставлен другим пользователем");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CommentsEntity getCommentById(Long commentId) {
        return commentsRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentsEntity> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        EventsEntity event = eventsService.findEventById(eventId);
        return commentsRepository.findAllByEvent_Id(event.getId(), PageRequest.of(from, size));
    }
}
