package ru.yandex.practicum.ewmmainservice.requests.service;

import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;
import ru.yandex.practicum.ewmmainservice.requests.views.RequestView;

import java.util.List;

public interface RequestService {
    List<RequestView> getAllRequestsByUserId(Long userId);

    RequestEntity addRequest(Long userId, Long eventId);

    RequestEntity requestCancel(Long userId, Long requestId);
}
