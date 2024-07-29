package ru.yandex.practicum.ewmmainservice.user.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;

import java.util.List;

public interface UserService {
    Page<UserEntity> findAllUsers(Integer from, Integer size, List<Long> usersIds);

    UserEntity addUser(UserEntity userEntity);

    void deleteUser(Long userId);
}
