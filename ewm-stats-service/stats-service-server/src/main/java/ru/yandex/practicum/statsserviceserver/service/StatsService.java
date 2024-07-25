package ru.yandex.practicum.statsserviceserver.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.statsserviceserver.model.HitEntity;
import ru.yandex.practicum.statsserviceserver.model.view.StatsView;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsService {
    HitEntity addHit(HitEntity hitEntity);

    List<StatsView> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
