package pl.umcs.workshop.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.user.User;

import java.util.List;

// TODO: refactor /game to entire controller mapping
@RestController
@RequestMapping("game/")
public class GameController {
    GameService gameService;

    // TODO: change to manual wiring
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("admin/create")
    public @ResponseBody Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    // TODO: begin game method
    @PostMapping("{gameId}/admin/begin")
    // Returns list of round ids
    public Game beginGame(@PathVariable int gameId) {
        return gameService.beginGame(gameId);
    }

    @PostMapping("{gameId}/admin/end")
    public Game endGame(@PathVariable int gameId) {
        return gameService.endGame(gameId);
    }

    @PostMapping("{gameId}/join")
    public User joinGame(@PathVariable int gameId) {
        return gameService.joinGame(gameId);
    }

    @PostMapping("{gameId}/join/{userId}")
    public User joinGameAsUser(@PathVariable int gameId, @PathVariable int userId) {
        return gameService.joinGameAsUser(gameId, userId);
    }

    // TODO
    @PostMapping("{gameId}/admin/summary")
    // TODO: create summary config entity to pass as a parameter
    public List<Integer> generateGameSummary(@PathVariable int gameId) {
        return gameService.generateGameSummary(gameId);
    }

    // @GetMapping("admin/live-data/{gameId}")
    // Used for admin panel live monitoring
    // Contains: userId, score, generation, answer history [x+xx+++]
    // TODO: PlayerProfile class to store this info, return list of profiles

    // TODO: handle exclusion of a player (delete his data, destroy his cookie, log him out)
}
