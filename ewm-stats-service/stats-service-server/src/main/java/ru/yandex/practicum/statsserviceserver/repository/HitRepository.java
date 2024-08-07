package ru.yandex.practicum.statsserviceserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.statsserviceserver.model.HitEntity;
import ru.yandex.practicum.statsserviceserver.model.view.StatsView;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<HitEntity, Long> {
    @Query("SELECT new ru.yandex.practicum.statsserviceserver.model.view.StatsView(h.app, h.uri, count(h.ip)) " +
            "FROM HitEntity AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(h.ip) DESC")
    List<StatsView> getStats(@Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             @Param("uris") List<String> uris);

    @Query("SELECT new ru.yandex.practicum.statsserviceserver.model.view.StatsView(h.app, h.uri, count(DISTINCT h.ip)) " +
            "FROM HitEntity AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(DISTINCT h.ip) DESC")
    List<StatsView> getUniqueStats(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("SELECT count(DISTINCT h.ip) " +
            "FROM HitEntity AS h " +
            "WHERE h.uri = :uri " +
            "GROUP BY h.uri " +
            "ORDER BY count(DISTINCT h.ip) DESC")
    Long getViews(@Param("uri") String uri);
}
