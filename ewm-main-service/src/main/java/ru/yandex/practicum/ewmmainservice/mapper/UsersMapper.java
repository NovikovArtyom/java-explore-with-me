package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.ewmmainservice.user.dto.AddUserRequestDto;
import ru.yandex.practicum.ewmmainservice.user.dto.UserResponseDto;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UserEntity fromAddUserRequestDtoToUserEntity(AddUserRequestDto addUserRequestDto);

    UserResponseDto fromUserEntityToUserResponseDto(UserEntity userEntity);
}
