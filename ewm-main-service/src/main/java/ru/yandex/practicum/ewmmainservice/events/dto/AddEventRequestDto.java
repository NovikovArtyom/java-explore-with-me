package ru.yandex.practicum.ewmmainservice.events.dto;

import ru.yandex.practicum.ewmmainservice.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class AddEventRequestDto {
    @NotNull
    @NotBlank
    private String annotation;
    @NotNull
    @PositiveOrZero
    private Integer category;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    @NotBlank
    private String title;
}
