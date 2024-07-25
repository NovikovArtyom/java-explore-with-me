package ru.yandex.practicum.statsservicedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatsDtoResponse {
    private String app;
    private String uri;
    private Integer hits;
}
