package ru.yandex.practicum.statsserviceserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.statsserviceserver.model.HitEntity;
@Repository
public interface HitRepository extends JpaRepository<HitEntity, Long> {
}
