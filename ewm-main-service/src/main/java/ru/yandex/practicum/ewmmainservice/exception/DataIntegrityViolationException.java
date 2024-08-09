package ru.yandex.practicum.ewmmainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataIntegrityViolationException extends IllegalArgumentException {
    private final String message;
}
