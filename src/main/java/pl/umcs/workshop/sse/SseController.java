package pl.umcs.workshop.sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {
    // TODO: get cookie from request header
    @GetMapping("event")
    public SseEmitter handleSseConnection(Long userId) {
        return SseService.handleSseConnection(userId);
    }
}
