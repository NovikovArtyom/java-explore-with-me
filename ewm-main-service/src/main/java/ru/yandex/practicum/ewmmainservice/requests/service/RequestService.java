package ru.yandex.practicum.ewmmainservice.requests.service;

import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdateStatus;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;

import java.util.List;

public interface RequestService {
    List<RequestEntity> getAllRequestsByUserId(Long userId);

    RequestEntity addRequest(Long userId, Long eventId);

    RequestEntity requestCancel(Long userId, Long requestId);

    List<RequestEntity> getAllRequestsByEventIdByUserId(Long userId, Long eventId);

    RequestsDtoUpdateStatus updateRequestsStatus(Long userId, Long eventId, RequestsDtoUpdate requestsDtoUpdate);
}
