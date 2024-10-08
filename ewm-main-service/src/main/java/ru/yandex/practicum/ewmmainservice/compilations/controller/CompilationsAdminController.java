package ru.yandex.practicum.ewmmainservice.compilations.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoRequest;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoResponse;
import ru.yandex.practicum.ewmmainservice.compilations.dto.CompilationsDtoUpdate;
import ru.yandex.practicum.ewmmainservice.compilations.service.CompilationsService;
import ru.yandex.practicum.ewmmainservice.mapper.CompilationsMapper;

@RestController
@RequestMapping("/admin/compilations")
@Validated
@Slf4j
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
        log.info("Compilations. Admin Controller: 'addCompilation' method called");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationsService.addCompilation(compilationsDtoRequest))
        );
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<?> deleteCompilation(
            @Positive @PathVariable Long compId
    ) {
        log.info("Compilations. Admin Controller: 'deleteCompilation' method called");
        compilationsService.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationsDtoResponse> updateCompilation(
            @Positive @PathVariable Long compId,
            @Valid @RequestBody CompilationsDtoUpdate compilationsDtoUpdate
    ) {
        log.info("Compilations. Admin Controller: 'updateCompilation' method called");
        return ResponseEntity.ok(
                compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationsService.updateCompilation(compId, compilationsDtoUpdate))
        );
    }
}
