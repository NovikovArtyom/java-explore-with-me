package ru.yandex.practicum.ewmmainservice.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
