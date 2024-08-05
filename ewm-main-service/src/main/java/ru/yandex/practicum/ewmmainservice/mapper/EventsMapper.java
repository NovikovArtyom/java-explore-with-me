package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainservice.categories.dto.AddCategoriesResponseDto;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;
import ru.yandex.practicum.ewmmainservice.events.dto.AddEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.dto.EventResponseDto;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface EventsMapper {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventsEntity fromAddEventRequestDtoToEventsEntity(AddEventRequestDto addEventRequestDto);

    @Mapping(source = "categories", target = "category")
    @Mapping(source = "states", target = "state")
    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "createdOn", source = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", source = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventResponseDto fromEventsEntityToEventResponseDto(EventsEntity eventsEntity);

    AddCategoriesResponseDto fromCategoriesEntityToAddCategoriesResponseDto(CategoriesEntity categoriesEntity);
}
