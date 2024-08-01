package ru.yandex.practicum.ewmmainservice.requests.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.exception.RequestException;
import ru.yandex.practicum.ewmmainservice.exception.RequestNotFoundException;
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

    public RequestServiceImpl(RequestRepository requestRepository, UserService userService, EventsService eventsService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventsService = eventsService;
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
                            request.setStatus(RequestStatus.ACCEPTED);
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
}
