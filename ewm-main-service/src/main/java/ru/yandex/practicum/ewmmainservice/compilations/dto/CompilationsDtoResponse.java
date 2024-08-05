package ru.yandex.practicum.ewmmainservice.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationsDtoResponse {
    private List<EventResponseDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
