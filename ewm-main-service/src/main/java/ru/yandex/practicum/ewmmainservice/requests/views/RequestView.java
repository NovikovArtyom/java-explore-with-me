package ru.yandex.practicum.ewmmainservice.requests.views;

import ru.yandex.practicum.ewmmainservice.requests.model.RequestStatus;

import java.time.LocalDateTime;

public class RequestView {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
