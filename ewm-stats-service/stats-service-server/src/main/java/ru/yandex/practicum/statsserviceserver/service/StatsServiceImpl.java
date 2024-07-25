package ru.yandex.practicum.statsserviceserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.statsserviceserver.exception.IncorrectDateException;
import ru.yandex.practicum.statsserviceserver.model.HitEntity;
import ru.yandex.practicum.statsserviceserver.model.view.StatsView;
import ru.yandex.practicum.statsserviceserver.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final HitRepository hitRepository;

    public StatsServiceImpl(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    public HitEntity addHit(HitEntity hitEntity) {
        log.info("Запрос попал в метод сервиса - addHit");
        return hitRepository.save(hitEntity);
    }

    @Override
    public List<StatsView> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Запрос попал в метод сервиса - getStats");
        if (start.isAfter(end)) {
            throw new IncorrectDateException("Дата начала интервала поиска не может быть позднее даты окончания интервала поиска");
        }
        if (unique) {
            return hitRepository.getUniqueStats(start, end, uris);
        } else {
            return hitRepository.getStats(start, end, uris);
        }
    }
}
