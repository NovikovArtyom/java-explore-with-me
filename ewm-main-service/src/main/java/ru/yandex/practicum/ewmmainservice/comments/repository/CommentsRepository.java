package ru.yandex.practicum.ewmmainservice.comments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainservice.comments.model.CommentsEntity;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsEntity, Long> {
    Page<CommentsEntity> findAllByUser_Id(Long userId, Pageable pageable);

    Page<CommentsEntity> findAllByEvent_Id(Long eventId, Pageable pageable);
}
