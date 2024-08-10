package ru.yandex.practicum.ewmmainservice.requests.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.mapper.RequestMapper;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoResponse;
import ru.yandex.practicum.ewmmainservice.requests.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    public RequestController(RequestService requestService, RequestMapper requestMapper) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @GetMapping
    public ResponseEntity<List<RequestsDtoResponse>> getAllRequestsByUserId(
            @Positive @PathVariable Long userId
    ) {
        log.info("Requests. Controller: 'getAllRequestsByUserId' method called");
        return ResponseEntity.ok(
                requestService.getAllRequestsByUserId(userId).stream()
                        .map(requestMapper::fromRequestViewToRequestDtoResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<RequestsDtoResponse> addRequest(
            @Positive @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        log.info("Requests. Controller: 'addRequest' method called");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        requestMapper.fromRequestEntityToRequestDtoResponse(requestService.addRequest(userId, eventId))
                );
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestsDtoResponse> requestCancel(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long requestId
    ) {
        log.info("Requests. Controller: 'requestCancel' method called");
        return ResponseEntity.ok(
                requestMapper.fromRequestEntityToRequestDtoResponse(requestService.requestCancel(userId, requestId))
        );
    }
}
