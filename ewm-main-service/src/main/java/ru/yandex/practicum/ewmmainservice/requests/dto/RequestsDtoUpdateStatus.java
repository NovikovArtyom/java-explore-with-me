package ru.yandex.practicum.ewmmainservice.requests.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestsDtoUpdateStatus {
    private List<RequestsDtoResponse> confirmedRequests;
    private List<RequestsDtoResponse> rejectedRequests;
}
