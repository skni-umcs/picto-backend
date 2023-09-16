package pl.umcs.workshop.sse;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {
  @GetMapping("event")
  public SseEmitter handleSseConnection(@RequestParam String token) {
    return SseService.handleSseConnection(token);
  }
}
