package ru.yandex.practicum.ewmmainservice.events.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.categories.model.CategoriesEntity;
import ru.yandex.practicum.ewmmainservice.categories.service.CategoriesService;
import ru.yandex.practicum.ewmmainservice.events.dto.PatchEventRequestDto;
import ru.yandex.practicum.ewmmainservice.events.model.EventStateAction;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;
import ru.yandex.practicum.ewmmainservice.events.repository.EventsRepository;
import ru.yandex.practicum.ewmmainservice.exception.*;
import ru.yandex.practicum.ewmmainservice.location.model.LocationEntity;
import ru.yandex.practicum.ewmmainservice.location.repository.LocationRepository;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventsServiceImpl(EventsRepository eventsRepository, LocationRepository locationRepository, UserService userService, CategoriesService categoriesService) {
        this.eventsRepository = eventsRepository;
        this.locationRepository = locationRepository;
        this.userService = userService;
        this.categoriesService = categoriesService;
    }

    @Override
    @Transactional
    public EventsEntity addEvent(Long userId, EventsEntity eventsEntity, Long categoryId) {
        if (eventsEntity.getEventDate().isAfter(LocalDateTime.now())) {
            if (!userService.findAllUsers(0, 1, List.of(userId)).isEmpty()) {
                if (!eventsEntity.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                    LocationEntity location = locationRepository.save(eventsEntity.getLocation());
                    eventsEntity.setLocation(location);
                    eventsEntity.setConfirmedRequests(0);
                    eventsEntity.setCreatedOn(LocalDateTime.now());
                    eventsEntity.setInitiator(userService.findAllUsers(0, 1, List.of(userId)).getContent().get(0));
                    eventsEntity.setPublishedOn(LocalDateTime.now());
                    eventsEntity.setStates(EventsStates.PENDING);
                    eventsEntity.setViews(0);
                    eventsEntity.setCategories(categoriesService.findCategoriesById(categoryId));
                    if (eventsEntity.getPaid() == null) {
                        eventsEntity.setPaid(false);
                    }
                    if (eventsEntity.getParticipantLimit() == null) {
                        eventsEntity.setParticipantLimit(0);
                    }
                    if (eventsEntity.getRequestModeration() == null) {
                        eventsEntity.setRequestModeration(true);
                    }
                    return eventsRepository.save(eventsEntity);
                } else {
                    throw new IncorrectEventDateException("eventDate", "должно содержать дату, которая еще не наступила",
                            eventsEntity.getEventDate());
                }
            } else {
                throw new UserNotFoundException(userId);
            }
        } else {
            throw new DateTimeValidationException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsEntity> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        UserEntity user = userService.findUserById(userId);
        if (user != null) {
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
    @Transactional
    public EventsEntity patchEvent(Long userId, Long eventId, PatchEventRequestDto patchEventRequestDto) {
        LocalDateTime patchedDateTime = patchEventRequestDto.getEventDate() != null
                ? LocalDateTime.parse(patchEventRequestDto.getEventDate(), formatter) : null;
        if (patchedDateTime == null || patchedDateTime.isAfter(LocalDateTime.now())) {
            if (eventsRepository.existsByIdAndInitiator_Id(eventId, userId)) {
                EventsEntity event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
                if (!event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                    EventsEntity updatedEvent = patchingEvent(event, patchEventRequestDto);
                    if (event.getStates().equals(EventsStates.PENDING)) {
                        if (patchEventRequestDto.getStateAction() != null
                                && patchEventRequestDto.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
                            updatedEvent.setStates(EventsStates.CANCELED);
                        } else if (patchEventRequestDto.getStateAction() != null
                                && patchEventRequestDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                            updatedEvent.setStates(EventsStates.PUBLISHED);
                        }
                    } else if (event.getStates().equals(EventsStates.CANCELED)) {
                        if (patchEventRequestDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                            updatedEvent.setStates(EventsStates.PENDING);
                        }
                    } else {
                        throw new IncorrectEventStateException();
                    }
                    return eventsRepository.save(updatedEvent);
                } else {
                    throw new IncorrectEventDateException("eventDate", "должно содержать дату, которая еще не наступила",
                            event.getEventDate());
                }
            } else {
                throw new EventNotFoundException(eventId);
            }
        } else {
            throw new DateTimeValidationException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsEntity> getAllEvents(
            List<Long> users, List<EventsStates> states, List<Long> categories, String rangeStart, String rangeEnd,
            Integer from, Integer size
    ) {
        LocalDateTime start = rangeStart == null ? null : LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, formatter);
        if (start == null || end == null) {
            return eventsRepository.getAllEventsWithoutDate(users, states, categories, PageRequest.of(from, size));
        } else {
            return eventsRepository.getAllEvents(users, states, categories, start,
                    end, PageRequest.of(from, size));
        }
    }

    @Override
    @Transactional
    public EventsEntity patchEventStatus(Long eventId, PatchEventRequestDto patchEventRequestDto) {
        LocalDateTime patchedDateTime = patchEventRequestDto.getEventDate() != null
                ? LocalDateTime.parse(patchEventRequestDto.getEventDate(), formatter) : null;
        if (patchedDateTime == null || patchedDateTime.isAfter(LocalDateTime.now())) {
            EventsEntity event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
            if (!event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                if (patchEventRequestDto.getStateAction() != null) {
                    if (patchEventRequestDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT) &&
                            (event.getStates().equals(EventsStates.PENDING))) {
                        EventsEntity updatedEvent = patchingEvent(event, patchEventRequestDto);
                        updatedEvent.setStates(EventsStates.PUBLISHED);
                        event = eventsRepository.save(updatedEvent);
                    } else if (patchEventRequestDto.getStateAction().equals(EventStateAction.REJECT_EVENT) &&
                            event.getStates().equals(EventsStates.PENDING)) {
                        EventsEntity updatedEvent = patchingEvent(event, patchEventRequestDto);
                        updatedEvent.setStates(EventsStates.CANCELED);
                        event = eventsRepository.save(updatedEvent);
                    } else {
                        throw new DataIntegrityViolationException("Некорректные параметры изменения статуса!");
                    }
                }
                return event;
            } else {
                throw new IncorrectEventDateException("createdOn", "должно содержать дату, которая еще не наступила",
                        event.getCreatedOn());
            }
        } else {
            throw new DateTimeValidationException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsEntity> getAllEventsWithFiltration(
            String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable,
            String sort, Integer from, Integer size
    ) {
        try {
            categories.forEach(categoriesService::findCategoriesById);
        } catch (CategoriesNotFoundException e) {
            throw new CategoriesValidationException();
        }

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;
        String formattedText = "%" + text.toLowerCase() + "%";
        if (start == null || end == null) {
            return eventsRepository.getAllEventsWithSortWithoutDate(formattedText, categories,
                    paid, onlyAvailable, sort, PageRequest.of(from, size));
        } else {
            return eventsRepository.getAllEventsWithSortWithDate(formattedText, categories, paid,
                    onlyAvailable, start, end, sort, PageRequest.of(from, size));
        }

    }

    @Override
    @Transactional
    public EventsEntity getEventsById(Long id, Integer views) {
        EventsEntity event = eventsRepository.findByIdAndStates(id, EventsStates.PUBLISHED);
        if (event != null) {
            event.setViews(views != null ? views : 0);
            return eventsRepository.save(event);
        } else {
            throw new EventNotFoundException(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventsEntity findEventById(Long id) {
        return eventsRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existByCategoryId(Long catId) {
        return eventsRepository.existsByCategories_Id(catId);
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
