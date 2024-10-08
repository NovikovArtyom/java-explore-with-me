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

    @Query("select ev from EventsEntity as ev where " +
            "((:users) is null or ev.initiator.id in (:users)) " +
            "and ((:states) is null or ev.states in (:states)) " +
            "and ((:categories) is null or ev.categories.id in (:categories))")
    Page<EventsEntity> getAllEventsWithoutDate(@Param("users") List<Long> users,
                                    @Param("states") List<EventsStates> states,
                                    @Param("categories") List<Long> categories,
                                    Pageable pageable);

    @Query("select ev from EventsEntity as ev where " +
            "ev.id = :id and ev.states = :state")
    EventsEntity findByIdAndStates(@Param("id") Long id,
                                   @Param("state") EventsStates state);

    @Query("select ev from EventsEntity as ev where " +
            "(lower(ev.annotation) like :text or lower(ev.description) like :text) " +
            "and ((:categories) is null or ev.categories.id in (:categories)) " +
            "and (:paid is null or ev.paid=:paid) " +
            "and (ev.eventDate > current_timestamp) " +
            "and ((:onlyAvailable = false) or (ev.participantLimit > ev.confirmedRequests)) " +
            "and (ev.states='PUBLISHED') " +
            "order by case when :sort='EVENT_DATE' then ev.eventDate end, " +
            "case when :sort='VIEWS' then ev.views end")
    Page<EventsEntity> getAllEventsWithSortWithoutDate(@Param("text") String text,
                                            @Param("categories") List<Long> categories,
                                            @Param("paid") Boolean paid,
                                            @Param("onlyAvailable") Boolean onlyAvailable,
                                            @Param("sort") String sort,
                                            Pageable pageable);

    @Query("select ev from EventsEntity as ev where " +
            "(lower(ev.annotation) like :text) " +
            "and ((:categories) is null or ev.categories.id in (:categories)) " +
            "and (:paid is null or ev.paid=:paid) " +
            "and (ev.eventDate between :start and :end) " +
            "and ((:onlyAvailable = false) or (ev.participantLimit > ev.confirmedRequests)) " +
            "and (ev.states='PUBLISHED') " +
            "order by case when :sort='EVENT_DATE' then ev.eventDate end, " +
            "case when :sort='VIEWS' then ev.views end")
    Page<EventsEntity> getAllEventsWithSortWithDate(@Param("text") String text,
                                                       @Param("categories") List<Long> categories,
                                                       @Param("paid") Boolean paid,
                                                       @Param("onlyAvailable") Boolean onlyAvailable,
                                                       @Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end,
                                                       @Param("sort") String sort,
                                                       Pageable pageable);

    Boolean existsByCategories_Id(Long catId);

}
