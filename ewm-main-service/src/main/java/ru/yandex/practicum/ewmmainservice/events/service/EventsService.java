package ru.yandex.practicum.ewmmainservice.events.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.ewmmainservice.events.dto.PatchEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;

import java.util.List;

public interface EventsService {
    EventsEntity addEvent(Long userId, EventsEntity eventsEntity, Long categoryId);

    Page<EventsEntity> getAllEventsByUserId(Long userId, Integer from, Integer size);

    EventsEntity getEventsByIdByUserId(Long userId, Long eventId);

    EventsEntity patchEvent(Long userId, Long eventId, PatchEventRequestDto patchEventRequestDto);

    Page<EventsEntity> getAllEvents(List<Long> users, List<EventsStates> states, List<Long> categories,
                                    String rangeStart, String rangeEnd, Integer from, Integer size);

    EventsEntity patchEventStatus(Long eventId, PatchEventRequestDto patchEventRequestDto);

    Page<EventsEntity> getAllEventsWithFiltration(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                                  Integer size);

    EventsEntity getEventsById(Long id);

}

