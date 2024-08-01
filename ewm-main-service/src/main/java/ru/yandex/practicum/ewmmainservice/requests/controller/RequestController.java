package ru.yandex.practicum.ewmmainservice.requests.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.mapper.RequestMapper;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoResponse;
import ru.yandex.practicum.ewmmainservice.requests.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@Validated
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
        return ResponseEntity.ok(
                requestService.getAllRequestsByUserId(userId).stream()
                        .map(requestMapper::fromRequestViewToRequestDtoResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<RequestsDtoResponse> addRequest(
            @Positive @PathVariable Long userId,
            @Positive @RequestParam Long eventId
    ) {
        return ResponseEntity.ok(
                requestMapper.fromRequestEntityToRequestDtoResponse(requestService.addRequest(userId, eventId))
        );
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestsDtoResponse> requestCancel(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(
                requestMapper.fromRequestEntityToRequestDtoResponse(requestService.requestCancel(userId, requestId))
        );
    }
}
