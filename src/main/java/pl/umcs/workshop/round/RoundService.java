package pl.umcs.workshop.round;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;
import pl.umcs.workshop.user.UserInfo;

@Service
public class RoundService {
    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private GameRepository gameRepository;

    public Round getRoundSpeakerInfo(int roundId) {
        // Get and return round speaker data from the database
        Round round = null;

        return round;
    }

    public Round getRoundListenerInfo(int roundId) {
        // Get and return round listener data from the database
        Round round = null;

        return round;
    }

    // Speaker is always user_1
    public Round saveRoundSpeakerInfo(@NotNull UserInfo userInfo) {
        Round round = roundRepository.findById(userInfo.getRoundId()).orElse(null);

        assert round != null;
        round.setUserOneAnswerTime(userInfo.getAnswerTime());

        return roundRepository.save(round);
    }

    public Round saveRoundListenerInfo(@NotNull UserInfo userInfo) {
        Round round = roundRepository.findById(userInfo.getRoundId()).orElse(null);

        assert round != null;

        round.setUserTwoAnswerTime(userInfo.getAnswerTime());
        round.setImageSelected(userInfo.getImageSelected());

        return roundRepository.save(round);
    }

    public int getRoundResult(int roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);

        assert round != null;
        Game game = gameRepository.findById(round.getGameId()).orElse(null);

        assert game != null;
        if (round.getImageSelected() == round.getTopic()) {
            return game.getCorrectAnswerPoints();
        }

        return game.getWrongAnswerPoints();
    }
}
