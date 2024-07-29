package ru.yandex.practicum.ewmmainservice.events.dto;

import ru.yandex.practicum.ewmmainservice.categories.dto.AddCategoriesResponseDto;
import ru.yandex.practicum.ewmmainservice.location.dto.LocationDto;
import ru.yandex.practicum.ewmmainservice.user.dto.UserResponseDto;

public class EventResponseDto {
    private String annotation;
    private AddCategoriesResponseDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserResponseDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Integer views;
}
