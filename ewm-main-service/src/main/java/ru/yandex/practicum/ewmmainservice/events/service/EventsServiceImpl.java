package ru.yandex.practicum.ewmmainservice.events.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;
import ru.yandex.practicum.ewmmainservice.categories.service.CategoriesService;
import ru.yandex.practicum.ewmmainservice.events.dto.PatchEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.model.EventStateAction;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.events.repository.EventsRepository;
import ru.yandex.practicum.ewmmainservice.exception.EventNotFoundException;
import ru.yandex.practicum.ewmmainservice.exception.IncorrectEventDateException;
import ru.yandex.practicum.ewmmainservice.exception.IncorrectEventStateException;
import ru.yandex.practicum.ewmmainservice.exception.UserNotFoundException;
import ru.yandex.practicum.ewmmainservice.location.model.LocationEntity;
import ru.yandex.practicum.ewmmainservice.location.repository.LocationRepository;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;
import ru.yandex.practicum.ewmmainservice.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {
    private final EventsRepository eventsRepository;
    private final LocationRepository locationRepository;
    private final UserService userService;
    private final CategoriesService categoriesService;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventsServiceImpl(EventsRepository eventsRepository, LocationRepository locationRepository, UserService userService, CategoriesService categoriesService) {
        this.eventsRepository = eventsRepository;
        this.locationRepository = locationRepository;
        this.userService = userService;
        this.categoriesService = categoriesService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventsEntity addEvent(Long userId, EventsEntity eventsEntity) {
        if (userService.findAllUsers(0, 1, List.of(userId)).isEmpty()) {
            if (!eventsEntity.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                LocationEntity location = locationRepository.save(eventsEntity.getLocation());
                eventsEntity.setLocation(location);
                eventsEntity.setConfirmedRequests(0);
                eventsEntity.setCreatedOn(LocalDateTime.now());
                eventsEntity.setInitiator(userService.findAllUsers(0, 1, List.of(userId)).getContent().get(0));
                eventsEntity.setPublishedOn(LocalDateTime.now());
                eventsEntity.setStates(EventsStates.AWAITING_MODERATION);
                eventsEntity.setViews(0);
                return eventsRepository.save(eventsEntity);
            } else {
                throw new IncorrectEventDateException("eventDate", "должно содержать дату, которая еще не наступила",
                        eventsEntity.getEventDate());
            }
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsEntity> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        if (!userService.findAllUsers(0, 1, List.of(userId)).isEmpty()) {
            return eventsRepository.findAllByInitiator_Id(userId, PageRequest.of(from, size));
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventsEntity getEventsByIdByUserId(Long userId, Long eventId) {
        if (eventsRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            return eventsRepository.findByIdAndInitiator_Id(eventId, userId);
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventsEntity patchEvent(Long userId, Long eventId, PatchEventRequestDto patchEventRequestDto) {
        if (eventsRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            LocalDateTime eventDateTime = LocalDateTime.parse(patchEventRequestDto.getEventDate(), formatter);
            if (!eventDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                EventsEntity event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
                if (event.getStates().equals(EventsStates.AWAITING_MODERATION) || event.getStates().equals(EventsStates.REJECTED)) {
                    EventsEntity updatedEvent = patchingEvent(event, patchEventRequestDto);
                    return eventsRepository.save(updatedEvent);
                } else {
                    throw new IncorrectEventStateException();
                }
            } else {
                throw new IncorrectEventDateException("eventDate", "должно содержать дату, которая еще не наступила",
                        eventDateTime);
            }
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsEntity> getAllEvents(
            List<Long> users, List<EventsStates> states, List<Long> categories, String rangeStart, String rangeEnd,
            Integer from, Integer size
    ) {
        return eventsRepository.getAllEvents(users, states, categories, LocalDateTime.parse(rangeStart, formatter),
                LocalDateTime.parse(rangeEnd, formatter), PageRequest.of(from, size));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventsEntity patchEventStatus(Long eventId, PatchEventRequestDto patchEventRequestDto) {
        EventsEntity event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (!event.getCreatedOn().isBefore(LocalDateTime.now().plusHours(1))) {
            if (patchEventRequestDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT) &&
                    (event.getStates().equals(EventsStates.AWAITING_MODERATION))) {
                EventsEntity updatedEvent = patchingEvent(event, patchEventRequestDto);
                updatedEvent.setStates(EventsStates.PUBLISHED);
                return eventsRepository.save(updatedEvent);
            } else if (patchEventRequestDto.getStateAction().equals(EventStateAction.CANCEL_REVIEW) &&
                    (!event.getStates().equals(EventsStates.PUBLISHED))) {
                EventsEntity updatedEvent = patchingEvent(event, patchEventRequestDto);
                updatedEvent.setStates(EventsStates.REJECTED);
                return eventsRepository.save(updatedEvent);
            } else {
                throw new IncorrectEventStateException();
            }
        } else {
            throw new IncorrectEventDateException("createdOn", "должно содержать дату, которая еще не наступила",
                    event.getCreatedOn());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsEntity> getAllEventsWithFiltration(
            String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable,
            String sort, Integer from, Integer size
    ) {
        return eventsRepository.getAllEventsWithSort("%" + text.toLowerCase() + "%", categories, paid,
                LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter), onlyAvailable,
                sort, PageRequest.of(from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public EventsEntity getEventsById(Long id) {
        if (eventsRepository.existsById(id)) {
            return eventsRepository.findByIdAndStates(id, EventsStates.PUBLISHED);
        } else {
            throw new EventNotFoundException(id);
        }
    }

    private EventsEntity patchingEvent(EventsEntity eventsEntity, PatchEventRequestDto patchEventRequestDto) {
        if (patchEventRequestDto.getAnnotation() != null) {
            eventsEntity.setAnnotation(patchEventRequestDto.getAnnotation());
        }
        if (patchEventRequestDto.getCategory() != null) {
            CategoriesEntity categories = categoriesService.findCategoriesById(patchEventRequestDto.getCategory());
            eventsEntity.setCategories(categories);
        }
        if (patchEventRequestDto.getDescription() != null) {
            eventsEntity.setDescription(patchEventRequestDto.getDescription());
        }
        if (patchEventRequestDto.getEventDate() != null) {
            eventsEntity.setEventDate(LocalDateTime.parse(patchEventRequestDto.getEventDate(), formatter));
        }
        if (patchEventRequestDto.getLocation() != null) {
            LocationEntity location = eventsEntity.getLocation();
            if (patchEventRequestDto.getLocation().getLat() != null) {
                location.setLat(patchEventRequestDto.getLocation().getLat());
            }
            if (patchEventRequestDto.getLocation().getLon() != null) {
                location.setLon(patchEventRequestDto.getLocation().getLon());
            }
            locationRepository.save(location);
        }
        if (patchEventRequestDto.getPaid() != null) {
            eventsEntity.setPaid(patchEventRequestDto.getPaid());
        }
        if (patchEventRequestDto.getParticipantLimit() != null) {
            eventsEntity.setParticipantLimit(patchEventRequestDto.getParticipantLimit());
        }
        if (patchEventRequestDto.getRequestModeration() != null) {
            eventsEntity.setRequestModeration(patchEventRequestDto.getRequestModeration());
        }
        if (patchEventRequestDto.getTitle() != null) {
            eventsEntity.setTitle(patchEventRequestDto.getTitle());
        }
        return eventsEntity;
    }
}
