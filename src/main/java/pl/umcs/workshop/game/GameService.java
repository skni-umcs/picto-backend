package pl.umcs.workshop.game;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameService {
    List<Game> games = new ArrayList<Game>(Arrays.asList(
            new Game(0, 3, 3, 1, 0, 0),
            new Game(1, 2, 4, 1, -1, 2),
            new Game(2, 3, 4, 2, 0, 1),
            new Game(3, 2, 2, 1, 0, 0)
    ));

    public Game startNewGame(Game game) {
        // TODO Send game config to db and return game id

        return new Game(0, 0, 0, 0, 0, 0);
    }
}
