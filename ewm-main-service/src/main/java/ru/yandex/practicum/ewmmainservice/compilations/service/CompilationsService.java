package ru.yandex.practicum.ewmmainservice.compilations.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoRequest;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoResponse;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.compilations.model.CompilationsEntity;

import java.util.List;

public interface CompilationsService {
    CompilationsEntity addCompilation(CompilationsDtoRequest compilationsDtoRequest);

    void deleteCompilation(Long compId);

    CompilationsEntity updateCompilation(Long compId, CompilationsDtoUpdate compilationsDtoUpdate);

    Page<CompilationsEntity> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationsEntity getCompilationById(Long compId);
}
