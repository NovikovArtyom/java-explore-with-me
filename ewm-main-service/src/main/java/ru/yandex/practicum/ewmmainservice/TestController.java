package ru.yandex.practicum.ewmmainservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.statsserviceclient.client.StatsClient;
import ru.yandex.practicum.statsservicedto.HitDtoRequest;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class TestController {
    private final StatsClient statsClient;

    public TestController(StatsClient statsClient) {
        this.statsClient = statsClient;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> hit(@RequestBody HitDtoRequest hitDtoRequest) {
        return statsClient.addHit(hitDtoRequest);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> stats(@RequestParam(required = true) LocalDateTime start,
                                                  @RequestParam(required = true)LocalDateTime end,
                                                  @RequestParam(required = false) List<String> uris,
                                                  @RequestParam(required = false, defaultValue = "false")Boolean unique) throws UnsupportedEncodingException {
        return statsClient.getStats(start, end, uris, unique);
    }
}
