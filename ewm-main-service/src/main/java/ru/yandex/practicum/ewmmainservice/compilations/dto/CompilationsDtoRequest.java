package ru.yandex.practicum.ewmmainservice.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
