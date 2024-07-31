package ru.yandex.practicum.ewmmainservice.events.dto;

import lombok.Getter;
import ru.yandex.practicum.ewmmainservice.events.model.EventStateAction;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;

@Getter
public class PatchEventRequestDto {
    @NotBlank
    private String annotation;
    private Long category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @NotBlank
    private String title;
}
