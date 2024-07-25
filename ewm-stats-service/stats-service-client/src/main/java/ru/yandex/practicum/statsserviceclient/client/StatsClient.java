package ru.yandex.practicum.statsserviceclient.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.statsserviceclient.constants.ApiPrefix;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Service
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${stats-service-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(HitDtoRequest hitDtoRequest) {
        return post(ApiPrefix.ADD_HIT_PREFIX, hitDtoRequest);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) throws UnsupportedEncodingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String encodedStart = URLEncoder.encode(start.format(formatter), StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(end.format(formatter), StandardCharsets.UTF_8);

        Map<String, Object> param = Map.of(
                "start", encodedStart,
                "end", encodedEnd,
                "uris", uris,
                "unique", unique
        );
        return get(ApiPrefix.GET_STATS_PREFIX, param);
    }
}
