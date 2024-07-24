package ru.yandex.practicum.statsserviceserver.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;
import ru.yandex.practicum.statsservicedto.HitDtoResponse;
import ru.yandex.practicum.statsserviceserver.model.HitEntity;

@Mapper
public interface StatsMapper {
    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    HitEntity hitDtoRequestToHitEntity(HitDtoRequest hitDtoRequest);
    HitDtoResponse hitEntityToHitDtoResponse(HitEntity hitEntity);
}
