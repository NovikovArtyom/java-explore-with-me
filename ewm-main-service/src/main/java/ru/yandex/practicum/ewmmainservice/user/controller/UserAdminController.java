package ru.yandex.practicum.ewmmainservice.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.mapper.UsersMapper;
import ru.yandex.practicum.ewmmainservice.user.dto.AddUserRequestDto;
import ru.yandex.practicum.ewmmainservice.user.dto.UserResponseDto;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
import ru.yandex.practicum.ewmmainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@Validated
public class UserAdminController {
    private final UserService userService;
    private final UsersMapper usersMapper;

    public UserAdminController(UserService userService, UsersMapper usersMapper) {
        this.userService = userService;
        this.usersMapper = usersMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers(
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam List<Long> usersIds
    ) {
        Page<UserEntity> response = userService.findAllUsers(from, size,usersIds);
        if (!response.isEmpty()) {
            return ResponseEntity.ok(response.stream().map(usersMapper::fromUserEntityToUserResponseDto).collect(Collectors.toList()));
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody AddUserRequestDto addUserRequestDto) {
        return ResponseEntity.ok(usersMapper.fromUserEntityToUserResponseDto(userService.addUser(
                usersMapper.fromAddUserRequestDtoToUserEntity(addUserRequestDto)
        )));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PositiveOrZero @PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
