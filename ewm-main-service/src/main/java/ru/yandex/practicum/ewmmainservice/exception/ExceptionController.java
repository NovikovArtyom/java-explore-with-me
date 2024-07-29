package ru.yandex.practicum.ewmmainservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final CategoriesNotFoundException e) {
        Long catId = e.getCatId();
        log.error(String.format("Категория с id = %s не зарегистрирована!", catId));
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                String.format("Category with id=%s was not found", catId), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final DataIntegrityViolationException e) {
        String message = e.getMessage();
        log.error("Ошибка уникальности!");
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), "Integrity constraint has been violated.",
                message, LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final UserNotFoundException e) {
        Long userId = e.getUserId();
        log.error(String.format("Пользователь с id = %s", userId));
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                String.format("User with id=%s was not found", userId), LocalDateTime.now().format(formatter));
    }
}
