package ru.yandex.practicum.statsservicedto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HitDtoResponse {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
