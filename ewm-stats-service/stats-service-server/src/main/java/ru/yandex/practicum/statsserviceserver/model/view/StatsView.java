package ru.yandex.practicum.statsserviceserver.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatsView {
    private String app;
    private String uri;
    private Long hits;
}
