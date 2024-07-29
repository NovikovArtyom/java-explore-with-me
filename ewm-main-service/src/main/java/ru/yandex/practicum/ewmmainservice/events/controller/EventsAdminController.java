package ru.yandex.practicum.ewmmainservice.events.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;

@RestController
@RequestMapping("/admin/events")
public class EventsAdminController {
    private final EventsService eventsService;

    public EventsAdminController(EventsService eventsService) {
        this.eventsService = eventsService;
    }
}
