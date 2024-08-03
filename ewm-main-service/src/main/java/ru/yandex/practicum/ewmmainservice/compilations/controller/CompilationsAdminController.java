package ru.yandex.practicum.ewmmainservice.compilations.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoRequest;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoResponse;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.compilations.service.CompilationsService;
import ru.yandex.practicum.ewmmainservice.mapper.CompilationsMapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@Validated
public class CompilationsAdminController {
    private final CompilationsService compilationsService;
    private final CompilationsMapper compilationsMapper;

    public CompilationsAdminController(CompilationsService compilationsService, CompilationsMapper compilationsMapper) {
        this.compilationsService = compilationsService;
        this.compilationsMapper = compilationsMapper;
    }

    @PostMapping
    public ResponseEntity<CompilationsDtoResponse> addCompilation(
            @Valid @RequestBody CompilationsDtoRequest compilationsDtoRequest
    ) {
        return ResponseEntity.ok(
                compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationsService.addCompilation(compilationsDtoRequest))
        );
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(
            @Positive @PathVariable Long compId
    ) {
        compilationsService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationsDtoResponse> updateCompilation(
            @Positive @PathVariable Long compId,
            @RequestBody CompilationsDtoUpdate compilationsDtoUpdate
    ) {
        return ResponseEntity.ok(
                compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationsService.updateCompilation(compId, compilationsDtoUpdate))
        );
    }
}