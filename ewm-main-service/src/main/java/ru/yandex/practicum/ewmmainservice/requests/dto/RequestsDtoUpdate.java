package ru.yandex.practicum.ewmmainservice.requests.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestsDtoUpdate {
    @NotNull
    @NotEmpty
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
