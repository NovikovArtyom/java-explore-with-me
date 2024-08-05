package ru.yandex.practicum.ewmmainservice.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.ewmmainservice.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddEventRequestDto {
    @NotNull
    @NotBlank
    private String annotation;
    @NotNull
    @PositiveOrZero
    private Long category;
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
