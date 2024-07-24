package ru.yandex.practicum.statsservicedto;

import lombok.Getter;

@Getter
public class HitDtoResponse {
    private String app;
    private String uri;
    private Integer hits;
}
