package ru.yandex.practicum.ewmmainservice.requests.dto;

import lombok.Getter;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class RequestsDtoUpdate {
    @NotNull
    @NotEmpty
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
