package ru.yandex.practicum.ewmmainservice.requests.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestsDtoResponse {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
