package ru.yandex.practicum.ewmmainservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequestDto {
    @NotNull
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
