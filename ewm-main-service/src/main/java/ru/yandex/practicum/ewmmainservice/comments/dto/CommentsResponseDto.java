package ru.yandex.practicum.ewmmainservice.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponseDto {
    private Long id;
    private String text;
    private String published;
    private Long userId;
    private Long eventId;
}
