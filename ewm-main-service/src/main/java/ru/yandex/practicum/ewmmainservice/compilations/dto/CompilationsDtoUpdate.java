package ru.yandex.practicum.ewmmainservice.compilations.dto;

import jakarta.validation.constraints.Size;
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
    @Size(max = 50)
    private String title;
}
