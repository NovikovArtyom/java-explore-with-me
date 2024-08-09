package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoResponse;
import ru.yandex.practicum.ewmmainservice.compilations.model.CompilationsEntity;

@Mapper(componentModel = "spring")
public interface CompilationsMapper {
    CompilationsDtoResponse fromCompilationsEntityToCompilationsDtoResponse(CompilationsEntity compilationsEntity);
}
