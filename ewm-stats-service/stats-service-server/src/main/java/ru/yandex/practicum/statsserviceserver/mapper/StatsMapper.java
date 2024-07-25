package ru.yandex.practicum.statsserviceserver.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;
import ru.yandex.practicum.statsservicedto.HitDtoResponse;
import ru.yandex.practicum.statsservicedto.StatsDtoResponse;
import ru.yandex.practicum.statsserviceserver.model.HitEntity;
import ru.yandex.practicum.statsserviceserver.model.view.StatsView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsMapper {
    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public static HitEntity hitDtoRequestToHitEntity(HitDtoRequest hitDtoRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        return new HitEntity(
                null,
                hitDtoRequest.getApp(),
                hitDtoRequest.getUri(),
                hitDtoRequest.getIp(),
                LocalDateTime.parse(hitDtoRequest.getTimestamp(), formatter)
        );
    }

    public static HitDtoResponse hitEntityToHitDtoResponse(HitEntity hitEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        return new HitDtoResponse(
                hitEntity.getId(),
                hitEntity.getApp(),
                hitEntity.getUri(),
                hitEntity.getIp(),
                URLEncoder.encode(hitEntity.getTimestamp().format(formatter), StandardCharsets.UTF_8)
        );
    }

    public static StatsDtoResponse statsViewToStatsDtoResponse(StatsView statsView) {
        return new StatsDtoResponse(
                statsView.getApp(),
                statsView.getUri(),
                statsView.getHits()
        );
    }

    public static LocalDateTime asLocalDateTime(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern(FORMATTER));
    }
}
