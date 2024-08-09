package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoResponse;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestsDtoResponse fromRequestViewToRequestDtoResponse(RequestEntity requestEntity);

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestsDtoResponse fromRequestEntityToRequestDtoResponse(RequestEntity requestEntity);
}
