package ru.yandex.practicum.ewmmainservice.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
