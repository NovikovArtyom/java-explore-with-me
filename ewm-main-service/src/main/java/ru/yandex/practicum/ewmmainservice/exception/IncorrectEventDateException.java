package ru.yandex.practicum.ewmmainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class IncorrectEventDateException extends IllegalArgumentException {
    private final String field;
    private final String message;
    private final LocalDateTime value;
}
