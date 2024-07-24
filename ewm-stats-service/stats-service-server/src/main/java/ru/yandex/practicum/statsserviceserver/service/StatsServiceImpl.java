package ru.yandex.practicum.statsserviceserver.service;

import ru.yandex.practicum.statsserviceserver.model.HitEntity;
import ru.yandex.practicum.statsserviceserver.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

public class StatsServiceImpl implements StatsService {
    private final HitRepository hitRepository;

    public StatsServiceImpl(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    public HitEntity addHit(HitEntity hitEntity) {
        return hitRepository.save(hitEntity);
    }

    @Override
    public List<HitEntity> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris != null && unique) {
            //запрос к базе
        } else if (uris != null && !unique) {
            //запрос к базе
        } else if (uris == null && unique) {
            //запрос к базе
        } else {
            //запрос к базе
        }

        if (uris != null) {
            if (unique) {
                //запрос к базе
            } else {
                //запрос к базе
            }
        } else {
            if (unique) {
                //запрос к базе
            } else {
                //запрос к базе
            }
        }
    }
}
