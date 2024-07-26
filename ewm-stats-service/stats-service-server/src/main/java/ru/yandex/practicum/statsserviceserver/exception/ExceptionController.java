package ru.yandex.practicum.statsserviceserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final IncorrectDateException e) {
        log.error("Дата начала интервала поиска не может быть позднее даты окончания интервала поиска");
        return new ErrorResponse("error: ", "Дата начала интервала поиска не может быть позднее даты окончания интервала поиска");
    }
}
