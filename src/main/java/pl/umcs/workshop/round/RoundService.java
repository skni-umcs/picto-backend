package pl.umcs.workshop.round;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserInfo;
import pl.umcs.workshop.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class RoundService {
    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public Round getNextRound(Long userId) {
        // Check what generation the user is on
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Check if the game exists and is still in progress
        Game game = gameRepository.findById(user.getGameId()).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getEndDateTime() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game has ended");
        }

        return roundRepository.getNextRound(user.getGameId(), userId, user.getGeneration() + 1);
    }

    public List<Image> getSpeakerImages(Long roundId) {
        

        return null;
    }

    public Round getRoundListenerInfo(Long roundId) {
        // Get and return round listener data from the database

        return null;
    }

    // Speaker is always user_1
    public Round saveRoundSpeakerInfo(@NotNull UserInfo userInfo) {
        Round round = roundRepository.findById(userInfo.getRoundId()).orElse(null);

        if (round == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Round not found");
        }

        round.setUserOneAnswerTime(userInfo.getAnswerTime());

        return roundRepository.save(round);
    }

    public Round saveRoundListenerInfo(@NotNull UserInfo userInfo) {
        Round round = roundRepository.findById(userInfo.getRoundId()).orElse(null);

        if (round == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Round not found");
        }

        round.setUserTwoAnswerTime(userInfo.getAnswerTime());
        round.setImageSelected(userInfo.getImageSelected());

        return roundRepository.save(round);
    }

    public RoundResult getRoundResult(Long roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);

        if (round == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Round not found");
        }

        Game game = gameRepository.findById(round.getGameId()).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (isImageCorrect(round)) {
            return new RoundResult(RoundResult.Result.CORRECT, game.getCorrectAnswerPoints());
        }

        return new RoundResult(RoundResult.Result.WRONG, game.getWrongAnswerPoints());
    }

    public boolean isImageCorrect(@NotNull Round round) {
        return Objects.equals(round.getImageSelected(), round.getTopic());
    }
}
