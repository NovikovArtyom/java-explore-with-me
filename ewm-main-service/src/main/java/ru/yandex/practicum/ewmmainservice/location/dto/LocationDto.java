package ru.yandex.practicum.ewmmainservice.location.dto;

import javax.validation.constraints.NotNull;

public class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
