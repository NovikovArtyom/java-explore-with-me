package ru.yandex.practicum.ewmmainservice.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.ewmmainservice.events.model.EventStateAction;
import ru.yandex.practicum.ewmmainservice.location.dto.LocationDto;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchEventRequestDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    @PositiveOrZero
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
