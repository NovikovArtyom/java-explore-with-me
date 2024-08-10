package ru.yandex.practicum.ewmmainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotFoundException extends IllegalArgumentException {
    private Long userId;
}
