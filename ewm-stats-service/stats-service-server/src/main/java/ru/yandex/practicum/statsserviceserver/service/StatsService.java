package ru.yandex.practicum.statsserviceserver.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.statsservicedto.HitDtoResponse;
import ru.yandex.practicum.statsserviceserver.model.HitEntity;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsService {
    HitEntity addHit(HitEntity hitEntity);

    List<HitEntity> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
