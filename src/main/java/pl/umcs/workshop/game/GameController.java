package pl.umcs.workshop.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundController;
import pl.umcs.workshop.round.RoundService;

import java.util.List;

@RestController
public class GameController {
    GameService gameService;
    RoundController roundController;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("start")
    public Game startNewGame(@RequestBody Game game) {
        GameValidator.validate(game);

        game = gameService.startNewGame(game);

        // Generate rounds
        roundController.saveRounds(game.getId(), generateRounds(game));

        return game;
    }

    private List<Round> generateRounds(Game game) {
        return null;
    }
}
