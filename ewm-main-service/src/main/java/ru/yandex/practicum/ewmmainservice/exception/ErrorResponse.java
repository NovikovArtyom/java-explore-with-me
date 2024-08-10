package ru.yandex.practicum.ewmmainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
