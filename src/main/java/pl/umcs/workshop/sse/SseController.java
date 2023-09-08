package pl.umcs.workshop.sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {
    @GetMapping("event")
    public SseEmitter handleSseConnection(@RequestHeader("x-session") String token) {
        return SseService.handleSseConnection(token);
    }
}
