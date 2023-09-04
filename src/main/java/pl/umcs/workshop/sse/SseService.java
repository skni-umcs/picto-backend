package pl.umcs.workshop.sse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.umcs.workshop.utils.JwtCookieHandler;

@Service
public class SseService {
    private static final HashMap<Pair<Long, Long>, SseEmitter> userSessions = new HashMap<>();

    private static SseEmitter createNewSession() {
        return new SseEmitter();
    }

    public static void addUserSession(Long gameId, Long userId) {
    userSessions.put(new ImmutablePair<>(gameId, userId), createNewSession());
    }

    public static void emitEventForUser(Long gameId, Long userId, EventType eventType) throws IOException {
        SseEmitter emitter = userSessions.get(new ImmutablePair<>(gameId, userId));
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .id(String.valueOf(userId))
                .data(eventType);
        emitter.send(event);
    }

    public static void emitEventForAll(Long gameId, EventType eventType) throws IOException {
        for (Map.Entry<Pair<Long, Long>, SseEmitter> entry : userSessions.entrySet()) {
            Pair<Long, Long> userGamePair = entry.getKey();
            Long userGameId = userGamePair.getKey();

            if (userGameId.equals(gameId)) {
                emitEventForUser(gameId, userGamePair.getValue(), eventType);
            }
        }
    }

    public static SseEmitter handleSseConnection(String token) {
        Long gameId = JwtCookieHandler.getGameId(token);
        Long userId = JwtCookieHandler.getUserId(token);

        return userSessions.get(new ImmutablePair<>(gameId, userId));
    }

    public static void closeSseConnection(Long gameId, Long userId) {
        userSessions.get(new ImmutablePair<>(gameId, userId)).complete();
    }

    public static void closeSseConnectionForAll(Long gameId) {
        for (Map.Entry<Pair<Long, Long>, SseEmitter> entry : userSessions.entrySet()) {
            Pair<Long, Long> userGamePair = entry.getKey();
            Long userGameId = userGamePair.getKey();

            if (userGameId.equals(gameId)) {
                closeSseConnection(gameId, entry.getKey().getKey());
            }
        }
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
