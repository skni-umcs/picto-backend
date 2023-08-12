package pl.umcs.workshop.round;

import org.springframework.stereotype.Service;

@Service
public class RoundService {
    // TODO handle connection with the database

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

    public Round saveRoundSpeakerInfo(int roundId) {
        // Save round speaker data to the database
        Round round = null;

        return round;
    }

    public Round saveRoundListenerInfo(int roundId) {
        // Save round listener data to the database
        Round round = null;

        return round;
    }

    public boolean getRoundResult(int roundId) {
        // Check if topic was guessed correctly

        return true;
    }
}
