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

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("start")
    public int startNewGame(@RequestBody Game game) {
        return gameService.startNewGame(game);
    }
}
