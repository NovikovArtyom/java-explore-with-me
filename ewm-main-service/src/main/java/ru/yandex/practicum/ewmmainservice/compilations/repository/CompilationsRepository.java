package ru.yandex.practicum.ewmmainservice.compilations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainservice.compilations.model.CompilationsEntity;

@Repository
public interface CompilationsRepository extends JpaRepository<CompilationsEntity, Long> {
    @Query("select c from CompilationsEntity as c where " +
            "(:pinned is null or c.pinned = :pinned)")
    Page<CompilationsEntity> getAllCompilations(@Param("pinned") Boolean pinned, Pageable pageable);
}
