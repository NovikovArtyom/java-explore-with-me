package ru.yandex.practicum.ewmmainservice.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationsDtoUpdate {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
