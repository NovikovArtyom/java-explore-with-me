package ru.yandex.practicum.statsserviceserver.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatsView {
    private String app;
    private String uri;
    private Long hits;
}
