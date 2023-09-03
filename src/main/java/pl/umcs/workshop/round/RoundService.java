package pl.umcs.workshop.round;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameService;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.image.ImageRepository;
import pl.umcs.workshop.sse.SseService;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserInfo;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.user.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class RoundService {
    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    public Round getNextRound(Long userId) throws IOException {
        // Check what generation the user is on
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Check if the game exists and is still in progress
        gameService.getGame(user.getGame().getId());

        Round round = roundRepository.getNextRound(user.getGame().getId(), userId, user.getGeneration() + 1);

        // TODO: refactor
        SseService.EventType eventType = Objects.equals(round.getUserOne().getId(), userId) ? SseService.EventType.SPEAKER_READY : SseService.EventType.LISTENER_HOLD;

        SseService.emitEventForUser(userId, eventType);

        return round;
    }

    public List<Image> getImages(Long roundId, Long userId) {
        return imageRepository.findAllImagesForUser(roundId, userId);
    }

    public Round saveRoundSpeakerInfo(@NotNull UserInfo userInfo) throws IOException {
        Round round = getRound(userInfo.getRoundId());
        round.setUserOneAnswerTime(userInfo.getAnswerTime());

        userService.updateUserLastSeen(userInfo.getUserId());

        Round saveRound = roundRepository.save(round);

        SseService.emitEventForUser(round.getUserOne().getId(), SseService.EventType.SPEAKER_HOLD);
        SseService.emitEventForUser(round.getUserTwo().getId(), SseService.EventType.LISTENER_READY);

        return saveRound;
    }

    public Round saveRoundListenerInfo(@NotNull UserInfo userInfo) {
        Round round = getRound(userInfo.getRoundId());

        round.setUserTwoAnswerTime(userInfo.getAnswerTime());
        round.setImageSelected(userInfo.getImageSelected());

        userService.updateUserLastSeen(userInfo.getUserId());

        return roundRepository.save(round);
    }

    public RoundResult getRoundResult(Long roundId) throws IOException {
        Round round = getRound(roundId);
        Game game = gameService.getGame(round.getGame().getId());

        // TODO: think this through
        SseService.emitEventForUser(round.getUserOne().getId(), SseService.EventType.AWAITING_ROUND);
        SseService.emitEventForUser(round.getUserTwo().getId(), SseService.EventType.AWAITING_ROUND);

        if (isImageCorrect(round)) {
            return new RoundResult(RoundResult.Result.CORRECT, game.getCorrectAnswerPoints());
        }

        return new RoundResult(RoundResult.Result.WRONG, game.getWrongAnswerPoints());
    }

    private @NotNull Round getRound(Long roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);

        if (round == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Round not found");
        }

        return round;
    }

    public boolean isImageCorrect(@NotNull Round round) {
        return Objects.equals(round.getImageSelected(), round.getTopic());
    }
}
