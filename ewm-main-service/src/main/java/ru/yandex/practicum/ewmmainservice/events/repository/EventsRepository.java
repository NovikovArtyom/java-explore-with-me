package ru.yandex.practicum.ewmmainservice.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;

@Repository
public interface EventsRepository extends JpaRepository<EventsEntity, Long> {
}
