package ru.yandex.practicum.ewmmainservice.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
