package ru.yandex.practicum.ewmmainservice.compilations.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoRequest;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.compilations.model.CompilationsEntity;
import ru.yandex.practicum.ewmmainservice.compilations.repository.CompilationsRepository;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.service.EventsService;
import ru.yandex.practicum.ewmmainservice.exception.CompilationNotFoundException;
import ru.yandex.practicum.ewmmainservice.exception.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationsRepository compilationsRepository;
    private final EventsService eventsService;

    public CompilationsServiceImpl(CompilationsRepository compilationsRepository, EventsService eventsService) {
        this.compilationsRepository = compilationsRepository;
        this.eventsService = eventsService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompilationsEntity addCompilation(CompilationsDtoRequest compilationsDtoRequest) {
        try {
            CompilationsEntity compilations = new CompilationsEntity();
            List<EventsEntity> events = new ArrayList<>();
            if (compilationsDtoRequest.getPinned() == null) {
                compilations.setPinned(false);
            } else {
                compilations.setPinned(compilationsDtoRequest.getPinned());
            }
            compilations.setTitle(compilationsDtoRequest.getTitle());
            if (compilationsDtoRequest.getEvents() == null) {
                compilations.setEvents(null);
            } else {
                compilationsDtoRequest.getEvents().forEach(item -> {
                    events.add(eventsService.findEventById(item));
                });
            }
            compilations.setEvents(events);
            return compilationsRepository.save(compilations);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteCompilation(Long compId) {
        if (compilationsRepository.existsById(compId)) {
            compilationsRepository.deleteById(compId);
        } else {
            throw new CompilationNotFoundException(compId);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompilationsEntity updateCompilation(Long compId, CompilationsDtoUpdate compilationsDtoUpdate) {
        CompilationsEntity compilations = compilationsRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException(compId));
        if (compilationsDtoUpdate.getEvents() != null) {
            List<EventsEntity> eventsList = new ArrayList<>();
            compilationsDtoUpdate.getEvents().forEach(item -> {
                eventsList.add(eventsService.getEventsById(item));
            });
            compilations.setEvents(eventsList);
        }
        if (compilationsDtoUpdate.getPinned() != null) {
            compilations.setPinned(compilationsDtoUpdate.getPinned());
        }
        if (compilationsDtoUpdate.getTitle() != null) {
            compilations.setTitle(compilationsDtoUpdate.getTitle());
        }
        return compilationsRepository.save(compilations);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompilationsEntity> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationsRepository.getAllCompilations(pinned, PageRequest.of(from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationsEntity getCompilationById(Long compId) {
        return compilationsRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException(compId));
    }
}
