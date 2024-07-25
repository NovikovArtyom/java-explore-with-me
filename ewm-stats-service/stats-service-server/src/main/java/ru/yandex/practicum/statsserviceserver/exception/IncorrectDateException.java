package ru.yandex.practicum.statsserviceserver.exception;

public class IncorrectDateException extends IllegalArgumentException {
    public IncorrectDateException(String message) {
        super(message);
    }
}
