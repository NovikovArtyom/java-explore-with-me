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
        log.error(String.format("Пользователь с id = %s не зарегистрирован", userId));
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                String.format("User with id=%s was not found", userId), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final IncorrectEventDateException e) {
        String field = e.getField();
        String message = e.getMessage();
        String value = e.getValue().format(formatter);
        log.error("Условия не соблюдены!");
        return new ErrorResponse(HttpStatus.FORBIDDEN.toString(), "For the requested operation the conditions are not met.",
                String.format("Field: %s. Error: %s. Value: %s", field, message, value), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final IncorrectEventStateException e) {
        log.error("Условия не соблюдены!");
        return new ErrorResponse(HttpStatus.FORBIDDEN.toString(), "For the requested operation the conditions are not met.",
                "Only pending or canceled events can be changed", LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final EventNotFoundException e) {
        Long eventId = e.getEventId();
        log.error(String.format("Событие с id = %s не зарегистрировано", eventId));
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                String.format("Event with id=%s was not found", eventId), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final RequestException e) {
        String message = e.getMessage();
        String description = e.getDescription();
        log.error(message);
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), message, description, LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final RequestNotFoundException e) {
        Long requestId = e.getRequestId();
        log.error(String.format("Заявка с id = %s не зарегистрирована", requestId));
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                String.format("Request with id=%s was not found", requestId), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final CompilationNotFoundException e) {
        Long compId = e.getCompId();
        log.error(String.format("Подборка с id = %s не зарегистрирована!", compId));
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                String.format("Compilation with id=%s was not found", compId), LocalDateTime.now().format(formatter));
    }

}
