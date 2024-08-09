package ru.yandex.practicum.ewmmainservice.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestsDtoUpdateStatus {
    private List<RequestsDtoResponse> confirmedRequests;
    private List<RequestsDtoResponse> rejectedRequests;
}
