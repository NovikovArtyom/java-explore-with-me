package ru.yandex.practicum.ewmmainservice.comments.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.ewmmainservice.events.model.EventsEntity;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private LocalDateTime published;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private EventsEntity event;

}
