package ru.yandex.practicum.ewmmainservice.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.mapper.UsersMapper;
import ru.yandex.practicum.ewmmainservice.user.dto.AddUserRequestDto;
import ru.yandex.practicum.ewmmainservice.user.dto.UserResponseDto;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
import ru.yandex.practicum.ewmmainservice.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@Validated
@Slf4j
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
            @RequestParam(name = "ids", required = false) List<Long> usersIds
    ) {
        log.info("User. Admin Controller: 'findAllUsers' method called");
        Page<UserEntity> response = userService.findAllUsers(from, size,usersIds);
        if (!response.isEmpty()) {
            return ResponseEntity.ok(response.stream().map(usersMapper::fromUserEntityToUserResponseDto).collect(Collectors.toList()));
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody AddUserRequestDto addUserRequestDto) {
        log.info("User. Admin Controller: 'addUser' method called");
        return ResponseEntity.status(HttpStatus.CREATED).body(usersMapper.fromUserEntityToUserResponseDto(userService.addUser(
                usersMapper.fromAddUserRequestDtoToUserEntity(addUserRequestDto)
        )));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PositiveOrZero @PathVariable Long userId) {
        log.info("User. Admin Controller: 'deleteUser' method called");
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
