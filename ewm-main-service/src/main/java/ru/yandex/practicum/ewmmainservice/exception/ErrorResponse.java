package ru.yandex.practicum.ewmmainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    String status;
    String reason;
    String message;
    String timestamp;
}
