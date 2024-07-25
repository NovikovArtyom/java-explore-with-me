package ru.yandex.practicum.statsserviceserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HitEntity {
    @Id
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @Column(name="hit_timestamp")
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "HitEntity{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
