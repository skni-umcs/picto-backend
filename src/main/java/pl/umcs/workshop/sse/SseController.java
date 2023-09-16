package pl.umcs.workshop.sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class SseController {
  @GetMapping("event")
  public SseEmitter handleSseConnection(@RequestHeader("x-session") String token) {
    return SseService.handleSseConnection(token);
  }

  private SseEmitter emitter;
  @GetMapping("test")
  public SseEmitter test(@RequestHeader("x-session") String token) throws IOException {
    emitter = new SseEmitter(-1L);
    return emitter;
  }

  @GetMapping("test-event")
  public void testEvent() throws IOException {
    emitter.send(SseEmitter.event().name("FASDNBGARE").build());
  }
}
