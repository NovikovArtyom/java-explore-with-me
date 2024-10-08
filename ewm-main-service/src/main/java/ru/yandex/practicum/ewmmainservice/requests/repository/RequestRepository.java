package ru.yandex.practicum.ewmmainservice.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainservice.requests.model.RequestEntity;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    @Query("select r from RequestEntity as r where " +
            "r.requester.id=:userId")
    List<RequestEntity> findAllByUserId(Long userId);

    Boolean existsByEvent_IdAndRequester_Id(Long eventId, Long userId);

    RequestEntity findByIdAndRequester_Id(Long requestId, Long userId);

    @Query("select r from RequestEntity as r where " +
            "r.event.id = :eventId and r.event.initiator.id = :userId")
    List<RequestEntity> getAllRequestsByEventIdByUserId(@Param("eventId") Long eventId,
                                                        @Param("userId") Long userId);
}
