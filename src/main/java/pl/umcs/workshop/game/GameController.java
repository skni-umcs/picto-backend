package pl.umcs.workshop.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.user.User;

import java.util.List;

@RestController
public class GameController {
    GameService gameService;

    // TODO: change to manual wiring
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("game/create")
    public @ResponseBody Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    // TODO: begin game method
    @PostMapping("game/begin/{gameId}")
    // Returns list of round ids
    public List<Integer> beginGame(@PathVariable int gameId) {
        return gameService.beginGame(gameId);
    }

    @PostMapping("game/end/{gameId}")
    public Game endGame(@PathVariable int gameId) throws Exception {
        return gameService.endGame(gameId);
    }

    // TODO: what happens after switching computers to a given user
    @PostMapping("game/join/{gameId}")
    public User joinGame(@PathVariable int gameId) throws Exception {
        return gameService.joinGame(gameId);
    }

    // TODO
    @PostMapping("game/summary/{gameId}")
    // TODO: create summary config entity to pass as a parameter
    public List<Integer> generateGameSummary(@PathVariable int gameId) {
        return gameService.generateGameSummary(gameId);
    }

    // @GetMapping("game/live-data/{gameId}")
    // Used for admin panel live monitoring
    // Contains: userId, score, generation, answer history [x+xx+++]
    // TODO: PlayerProfile class to store this info, return list of profiles

    // TODO: handle exclusion of a player (delete his data, destroy his cookie, log him out)
}
