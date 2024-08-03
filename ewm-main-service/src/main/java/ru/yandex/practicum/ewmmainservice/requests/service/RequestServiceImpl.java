package ru.yandex.practicum.ewmmainservice.requests.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.exception.DataIntegrityViolationException;
import ru.yandex.practicum.ewmmainservice.exception.EventNotFoundException;
import ru.yandex.practicum.ewmmainservice.exception.RequestException;
import ru.yandex.practicum.ewmmainservice.exception.RequestNotFoundException;
import ru.yandex.practicum.ewmmainservice.mapper.RequestMapper;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdateStatus;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestStatus;
import ru.yandex.practicum.ewmmainservice.requests.repository.RequestRepository;
import ru.yandex.practicum.ewmmainservice.requests.views.RequestView;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
import ru.yandex.practicum.ewmmainservice.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventsService eventsService;
    private final RequestMapper requestMapper;

    public RequestServiceImpl(RequestRepository requestRepository, UserService userService, EventsService eventsService, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventsService = eventsService;
        this.requestMapper = requestMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestView> getAllRequestsByUserId(Long userId) {
        return requestRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestEntity addRequest(Long userId, Long eventId) {
        UserEntity user = userService.findUserById(userId);
        EventsEntity event = eventsService.getEventsById(eventId);
        if (!requestRepository.existsByEvent_IdAndRequester_Id(eventId, userId)) {
            if (!event.getInitiator().equals(user)) {
                if (event.getStates().equals(EventsStates.PUBLISHED)) {
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        RequestEntity request = new RequestEntity();
                        request.setCreated(LocalDateTime.now());
                        request.setRequester(user);
                        request.setEvent(event);
                        if (event.getRequestModeration()) {
                            request.setStatus(RequestStatus.PENDING);
                        } else {
                            request.setStatus(RequestStatus.CONFIRMED);
                        }
                        return requestRepository.save(request);
                    } else {
                        throw new RequestException("The event has reached its limit of participation requests.",
                                "The event has reached its limit of participation requests.");
                    }
                } else {
                    throw new RequestException("You can't participate in an unpublished event.",
                            "You can't participate in an unpublished event.");
                }
            } else {
                throw new RequestException("An event initiator cannot add a request to their event.",
                        "An event initiator cannot add a request to their event.");
            }
        } else {
            throw new RequestException("You can't add a repeat request.", "You can't add a repeat request.");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestEntity requestCancel(Long userId, Long requestId) {
        RequestEntity request = requestRepository.findByIdAndRequester_Id(requestId, userId);
        if (request != null) {
            request.setStatus(RequestStatus.REJECTED);
            return requestRepository.save(request);
        } else {
            throw new RequestNotFoundException(requestId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestEntity> getAllRequestsByEventIdByUserId(Long userId, Long eventId) {
        return requestRepository.getAllRequestsByEventIdByUserId(eventId, userId);
    }

    @Override
    public RequestsDtoUpdateStatus updateRequestsStatus(Long userId, Long eventId, RequestsDtoUpdate requestsDtoUpdate) {
        EventsEntity event = eventsService.getEventsByIdByUserId(userId, eventId);
        if (event != null) {
            RequestsDtoUpdateStatus requestsDtoUpdateStatus = new RequestsDtoUpdateStatus();
            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                return requestsDtoUpdateStatus;
            } else {
                requestsDtoUpdate.getRequestIds().forEach(item -> {
                    RequestEntity request = requestRepository.findById(item).orElseThrow(() -> new RequestNotFoundException(item));
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        if (request.getStatus() == RequestStatus.PENDING) {
                            request.setStatus(RequestStatus.CONFIRMED);
                            requestRepository.save(request);
                            requestsDtoUpdateStatus.getConfirmedRequests().add(requestMapper.fromRequestEntityToRequestDtoResponse(request));
                        }
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        requestRepository.save(request);
                        requestsDtoUpdateStatus.getRejectedRequests().add(requestMapper.fromRequestEntityToRequestDtoResponse(request));
                        throw new RequestException("For the requested operation the conditions are not met.",
                                "The participant limit has been reached");
                    }
                });
                return requestsDtoUpdateStatus;
            }
        } else {
            throw new EventNotFoundException(eventId);
        }
    }
}
