package ru.yandex.practicum.statsservicedto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HitDtoResponse {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
