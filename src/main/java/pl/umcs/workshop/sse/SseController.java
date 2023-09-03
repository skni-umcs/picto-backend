package pl.umcs.workshop.sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {
    @GetMapping("event")
    public SseEmitter handleSseConnection() {
        return SseService.handleSseConnection();
    }
}
