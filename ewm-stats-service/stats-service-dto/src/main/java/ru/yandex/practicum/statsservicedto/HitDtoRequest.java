package ru.yandex.practicum.statsservicedto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HitDtoRequest {
    @NotNull
    @NotBlank
    private String app;
    @NotNull
    @NotBlank
    private String uri;
    @NotNull
    @NotBlank
    private String ip;
    @NotNull
    @NotBlank
    private String timestamp;
}
