package ru.yandex.practicum.ewmmainservice.location.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
