package pl.umcs.workshop.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;

// TODO: move to separate package
@Service
public class SseService {
    private static HashMap<Long, SseEmitter> userSessions = new HashMap<>();

    public enum EventType {
        AWAITING_GAME_BEGIN,
        AWAITING_ROUND,
        AWAITING_RESULT,
        SPEAKER_READY,
        LISTENER_READY,
        SPEAKER_HOLD,
        LISTENER_HOLD,
        PAUSE_GAME,
        END_GAME
    }

    private static SseEmitter createNewSession() {
        return new SseEmitter();
    }

    public static void addUserSession(Long userId) {
        userSessions.put(userId, createNewSession());
    }

    // TODO: modify to only notify users from game with given id
    public static void emitEventForAll(EventType eventType) throws IOException {
        for (SseEmitter emitter : userSessions.values()) {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .data(eventType);
            emitter.send(event);
        }
    }

    public static void emitEventForUser(Long userId, EventType eventType) throws IOException {
        SseEmitter emitter = userSessions.get(userId);
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .id(String.valueOf(userId))
                .data(eventType);
        emitter.send(event);
    }

    public static SseEmitter handleSseConnection(Long userId) {
        return userSessions.get(userId);
    }
}
