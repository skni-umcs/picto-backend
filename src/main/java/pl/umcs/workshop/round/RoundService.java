package pl.umcs.workshop.round;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoundService {
    // TODO handle connection with the database
    public List<Round> getRounds(int gameId) {
        // TODO Get all rounds for given game

        return null;
    }

    public List<Round> saveRounds(int gameId, List<Round> rounds) {
        // TODO Save all rounds for the given game

        return null;
    }

    public Round getRound(int roundId) {
        // TODO Get data of the round with given id db

        return null;
    }

    public Round saveRound(int roundId, Round round) {
        // TODO Save data of round with given id to db

        return null;
    }
}
