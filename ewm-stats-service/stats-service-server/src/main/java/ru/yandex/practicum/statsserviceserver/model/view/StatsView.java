package ru.yandex.practicum.statsserviceserver.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatsView {
    private String app;
    private String uri;
    private Long hits;
}
