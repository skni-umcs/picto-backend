package pl.umcs.workshop.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game createGame(Game game) {
        gameRepository.save(game);

        return game;
    }

    public List<Integer> beginGame(int gameId) {
        // Get all players
        // Get game config
        // Generate brackets (generations and rounds)
        // TODO separate class to generate brackets based on topology
        List<Integer> roundIds = null;

        return roundIds;
    }

    public int endGame(int gameId) {
        // Sets end time
        // De-auth users (possibly remove cookies)

        return gameId;
    }

    public int joinGame(int gameId) {
        // Adds new player and gets their id from the database
        int userId = 0;

        return userId;
    }

    public List<Integer> generateGameSummary() {
        // Summarize data from the game using config given as the parameter
        // (e.g. number of generations, players to exclude etc.)
        // Generate points to use in chart

        return null;
    }
}
