package ru.yandex.practicum.ewmmainservice.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String text;
}
