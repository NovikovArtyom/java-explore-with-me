package ru.yandex.practicum.ewmmainservice.compilations.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CompilationsDtoUpdate {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
