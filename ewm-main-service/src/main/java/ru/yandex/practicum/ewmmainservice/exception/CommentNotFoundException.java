package ru.yandex.practicum.ewmmainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentNotFoundException extends IllegalArgumentException {
    private final Long commentId;
}
