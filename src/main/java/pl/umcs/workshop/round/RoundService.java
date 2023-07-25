package pl.umcs.workshop.round;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoundService {
    public List<Round> getRounds(int gameId) {
        // Get all rounds for given game

        return null;
    }

    public List<Round> saveRounds(int gameId, List<Round> rounds) {
        // Save all rounds for the given game

        return null;
    }

    public Round getRound(int roundId) {
        // Get data of the round with given id db

        return null;
    }

    public Round saveRound(int roundId, Round round) {
        // Save data of round with given id to db

        return null;
    }
}
