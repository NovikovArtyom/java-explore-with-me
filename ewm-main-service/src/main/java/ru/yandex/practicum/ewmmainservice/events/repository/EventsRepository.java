package ru.yandex.practicum.ewmmainservice.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.events.model.EventsStates;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<EventsEntity, Long> {
    Page<EventsEntity> findAllByInitiator_Id(Long userId, Pageable pageable);

    Boolean existsByIdAndInitiator_Id(Long eventId, Long userId);

    EventsEntity findByIdAndInitiator_Id(Long eventId, Long userId);

    @Query("select ev from EventsEntity as ev where " +
            "((:users) is null or ev.initiator.id in (:users)) " +
            "and ((:states) is null or ev.states in (:states)) " +
            "and ((:categories) is null or ev.categories.id in (:categories)) " +
            "and (ev.createdOn between :rangeStart and :rangeEnd)")
    Page<EventsEntity> getAllEvents(@Param("users") List<Long> users,
                                    @Param("states") List<EventsStates> states,
                                    @Param("categories") List<Long> categories,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    Pageable pageable);

    EventsEntity findByIdAndStates(Long eventId, EventsStates state);
}
