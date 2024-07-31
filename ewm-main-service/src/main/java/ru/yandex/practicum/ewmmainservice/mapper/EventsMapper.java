package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.ewmmainservice.events.dto.AddEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;

@Mapper(componentModel = "spring")
public interface EventsMapper {
    EventsEntity fromAddEventRequestDtoToEventsEntity(AddEventRequestDto addEventRequestDto);

    EventResponseDto fromEventsEntityToEventResponseDto(EventsEntity eventsEntity);
}
