package ru.yandex.practicum.ewmmainservice.events.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class EventsPrivateController {
    private final EventsService eventsService;

    public EventsPrivateController(EventsService eventsService) {
        this.eventsService = eventsService;
    }
}
