package pl.umcs.workshop.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class SseController {
    @GetMapping("event")
    public SseEmitter handleSseConnection() throws IOException {
        return SseService.handleSseConnection();
    }
}
