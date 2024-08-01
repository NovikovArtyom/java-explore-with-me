package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoResponse;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;
import ru.yandex.practicum.ewmmainservice.requests.views.RequestView;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestsDtoResponse fromRequestViewToRequestDtoResponse(RequestView requestView);

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestsDtoResponse fromRequestEntityToRequestDtoResponse(RequestEntity requestEntity);
}
