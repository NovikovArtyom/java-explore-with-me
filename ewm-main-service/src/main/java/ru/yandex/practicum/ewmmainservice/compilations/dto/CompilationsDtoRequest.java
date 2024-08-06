package ru.yandex.practicum.ewmmainservice.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationsDtoRequest {
    private List<Long> events;
    private Boolean pinned;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String title;
}
