package pl.umcs.workshop.utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

// TODO: move to separate package
@RestController
public class SSEHandler {
    // userId -> sseEmitter [hash map]

    // TODO: enum for different event types
    @GetMapping("emitter")
    public SseEmitter emitUserReadyEvent() throws IOException {
        // get userId from cookie
        Long userId = 1L;

        SseEmitter emitter = new SseEmitter();

        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .id(String.valueOf(userId))
                .name("User " + userId + " is ready")
                .data(true);
        emitter.send(event);

        return emitter;
    }
}
