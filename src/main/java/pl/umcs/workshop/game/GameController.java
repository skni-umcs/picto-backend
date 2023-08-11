package pl.umcs.workshop.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundController;
import pl.umcs.workshop.round.RoundService;

import java.util.List;

@RestController
public class GameController {
    GameService gameService;

    // TODO change to manual wiring
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("create")
    public @ResponseBody Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    @PostMapping("begin/{gameId}")
    // Returns list of rounds id
    public List<Integer> beginGame(@PathVariable int gameId) {
        return gameService.beginGame(gameId);
    }

    @PostMapping("end/{gameId}")
    // TODO swap this or check if this makes sense
    public int endGame(@PathVariable int gameId) {
        return gameService.endGame(gameId);
    }

    @PostMapping("join/{gameId}")
    // Returns player's user id
    public int joinGame(@PathVariable int gameId) {
        return gameService.joinGame(gameId);
    }

    @PostMapping("summary")
    // TODO create summary config entity to pass as a parameter
    public List<Integer> generateSummary() {
        return gameService.generateSummary();
    }

    // @GetMapping("game-data/{gameId}")
    // Used for admin panel live monitoring
    // Contains: userId, score, generation, answer history [x+xx+++]
    // TODO PlayerProfile class to store this info, return list of profiles

    // TODO handle exclusion of a player (delete his data, destroy his cookie, log him out)
}
