package pl.umcs.workshop.round;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserInfo;
import pl.umcs.workshop.user.UserRepository;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class RoundService {
    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public Round getNextRound(int userId) {
        // Check what generation the user is on
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return roundRepository.getNextRound(user.getGameId(), userId, user.getGeneration() + 1);
    }

    public Round getRoundSpeakerInfo(int roundId) {
        // Get and return round speaker data from the database

        return null;
    }

    public Round getRoundListenerInfo(int roundId) {
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

    public RoundResult getRoundResult(int roundId) {
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
        return round.getImageSelected() == round.getTopic();
    }
}
