package pl.umcs.workshop.sse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserService;
import pl.umcs.workshop.utils.JwtCookieHandler;

@Service
public class SseService {
  private static final HashMap<Long, SseEmitter> userSessions = new HashMap<>();

  private static UserService userService;

  private static @NotNull SseEmitter createNewSession() {
      return new SseEmitter(-1L);
  }

  public static void addUserSession(@NotNull User user) {
    userSessions.put(user.getId(), createNewSession());
  }

  public static void emitEventForUser(@NotNull User user, @NotNull EventType eventType)
      throws IOException {
    SseEmitter emitter = userSessions.get(user.getId());
    SseEmitter.SseEventBuilder event =
        SseEmitter.event()
            .id(String.valueOf(user.getId()))
            .name(eventType.toString())
            .data(eventType);

    emitter.send(event);
  }

  public static void emitEventForAll(Long gameId, EventType eventType) throws IOException {
    for (Map.Entry<Long, SseEmitter> entry : userSessions.entrySet()) {
      Long userId = entry.getKey();
      User user = userService.getUser(userId);

      if (user.getGame().getId().equals(gameId)) {
        emitEventForUser(user, eventType);
      }
    }
  }

  public static SseEmitter handleSseConnection(String token) {
    Long userId = JwtCookieHandler.getUserId(token);

    return userSessions.get(userId);
  }

  public static void closeSseConnection(Long userId) {
    userSessions.get(userId).complete();
    userSessions.remove(userId);
  }

  public static void closeSseConnectionForAll(Long gameId) {
    for (Map.Entry<Long, SseEmitter> entry : userSessions.entrySet()) {
      Long userId = entry.getKey();
      User user = userService.getUser(userId);

      if (user.getGame().getId().equals(gameId)) {
        closeSseConnection(userId);
      }
    }
  }

  @Autowired
  private void setUserService(UserService userService) {
    SseService.userService = userService;
  }

  public enum EventType {
    GAME_BEGIN,
    AWAITING_ROUND,
    SPEAKER_READY,
    LISTENER_READY,
    SPEAKER_HOLD,
    LISTENER_HOLD,
    RESULT_READY,
    PAUSE_GAME,
    END_GAME
  }
}
