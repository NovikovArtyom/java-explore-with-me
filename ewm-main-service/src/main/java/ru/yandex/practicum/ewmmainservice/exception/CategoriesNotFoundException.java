package ru.yandex.practicum.ewmmainservice.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoriesNotFoundException extends IllegalArgumentException {
    private final Long catId;
}
