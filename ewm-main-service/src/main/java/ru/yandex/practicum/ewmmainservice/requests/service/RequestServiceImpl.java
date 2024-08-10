package ru.yandex.practicum.ewmmainservice.requests.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.events.repository.EventsRepository;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.exception.*;
import ru.yandex.practicum.ewmmainservice.mapper.RequestMapper;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoResponse;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.requests.dto.RequestsDtoUpdateStatus;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestStatus;
import ru.yandex.practicum.ewmmainservice.requests.repository.RequestRepository;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
import ru.yandex.practicum.ewmmainservice.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventsService eventsService;
    private final RequestMapper requestMapper;
    private final EventsRepository eventsRepository;

    public RequestServiceImpl(RequestRepository requestRepository, UserService userService, EventsService eventsService, RequestMapper requestMapper, EventsRepository eventsRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventsService = eventsService;
        this.requestMapper = requestMapper;
        this.eventsRepository = eventsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestEntity> getAllRequestsByUserId(Long userId) {
        log.info("Requests. Service: 'getAllRequestsByUserId' method called");
        return requestRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public RequestEntity addRequest(Long userId, Long eventId) {
        log.info("Requests. Service: 'addRequest' method called");
        UserEntity user = userService.findUserById(userId);
        EventsEntity event = eventsService.findEventById(eventId);
        if (event.getStates().equals(EventsStates.PUBLISHED)) {
            if (!requestRepository.existsByEvent_IdAndRequester_Id(eventId, userId)) {
                if (!event.getInitiator().equals(user)) {
                    if (event.getStates().equals(EventsStates.PUBLISHED)) {
                        if ((event.getParticipantLimit() == 0) || (event.getConfirmedRequests() < event.getParticipantLimit())) {
                            RequestEntity request = new RequestEntity();
                            request.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                            request.setRequester(user);
                            request.setEvent(event);
                            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                                if (event.getParticipantLimit() != 0) {
                                    if (event.getConfirmedRequests() + 1 <= event.getParticipantLimit()) {
                                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                                        eventsRepository.save(event);
                                    } else {
                                        throw new RequestException("The event has reached its limit of participation requests.",
                                                "The event has reached its limit of participation requests.");
                                    }
                                }
                                request.setStatus(RequestStatus.CONFIRMED);
                            } else {
                                request.setStatus(RequestStatus.PENDING);
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
        } else {
            throw new DataIntegrityViolationException("Попытка добавить заявку к необупликованному событию!");
        }
    }

    @Override
    @Transactional
    public RequestEntity requestCancel(Long userId, Long requestId) {
        log.info("Requests. Service: 'requestCancel' method called");
        RequestEntity request = requestRepository.findByIdAndRequester_Id(requestId, userId);
        if (request != null) {
            request.setStatus(RequestStatus.CANCELED);
            return requestRepository.save(request);
        } else {
            throw new RequestNotFoundException(requestId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestEntity> getAllRequestsByEventIdByUserId(Long userId, Long eventId) {
        log.info("Requests. Service: 'getAllRequestsByEventIdByUserId' method called");
        return requestRepository.getAllRequestsByEventIdByUserId(eventId, userId);
    }

    @Override
    @Transactional
    public RequestsDtoUpdateStatus updateRequestsStatus(Long userId, Long eventId, RequestsDtoUpdate requestsDtoUpdate) {
        log.info("Requests. Service: 'updateRequestsStatus' method called");
        EventsEntity event = eventsService.getEventsByIdByUserId(userId, eventId);
        if (event != null) {
            RequestsDtoUpdateStatus updateStatus = new RequestsDtoUpdateStatus();
            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                return updateStatus;
            }

            List<RequestsDtoResponse> confirmedRequests = new ArrayList<>();
            List<RequestsDtoResponse> rejectedRequests = new ArrayList<>();

            for (Long requestId : requestsDtoUpdate.getRequestIds()) {
                RequestEntity request = requestRepository.findById(requestId)
                        .orElseThrow(() -> new RequestNotFoundException(requestId));

                if (request.getStatus() != RequestStatus.PENDING) {
                    throw new DataIntegrityViolationException("Попытка обновить событие с окончательным статусом!");
                }

                if (RequestStatus.CONFIRMED.equals(requestsDtoUpdate.getStatus())) {
                    handleConfirmedRequest(event, request, confirmedRequests);
                } else if (RequestStatus.REJECTED.equals(requestsDtoUpdate.getStatus())) {
                    handleRejectedRequest(request, rejectedRequests);
                } else {
                    throw new IncorrectEventStateException();
                }
            }

            updateStatus.setConfirmedRequests(confirmedRequests);
            updateStatus.setRejectedRequests(rejectedRequests);
            return updateStatus;
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    private void handleConfirmedRequest(EventsEntity event, RequestEntity request, List<RequestsDtoResponse> confirmedRequests) {
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new RequestException("The event has reached its limit of participation requests.",
                    "The participant limit has been reached");
        }

        request.setStatus(RequestStatus.CONFIRMED);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);

        requestRepository.save(request);
        eventsRepository.save(event);

        confirmedRequests.add(requestMapper.fromRequestEntityToRequestDtoResponse(request));
    }

    private void handleRejectedRequest(RequestEntity request, List<RequestsDtoResponse> rejectedRequests) {
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        rejectedRequests.add(requestMapper.fromRequestEntityToRequestDtoResponse(request));
    }
}
