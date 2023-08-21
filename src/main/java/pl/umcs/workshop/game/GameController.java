package pl.umcs.workshop.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.user.User;

import java.util.List;

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
    public Game beginGame(@PathVariable Long gameId) {
        return gameService.beginGame(gameId);
    }

    @PostMapping("{gameId}/join")
    public User joinGame(@PathVariable Long gameId) {
        return gameService.joinGame(gameId);
    }

    @PostMapping("{gameId}/join/{userId}")
    public User joinGameAsUser(@PathVariable Long gameId, @PathVariable Long userId) {
        return gameService.joinGameAsUser(gameId, userId);
    }

    @PostMapping("{gameId}/admin/end")
    public Game endGame(@PathVariable Long gameId) {
        return gameService.endGame(gameId);
    }

    // TODO
    @PostMapping("{gameId}/admin/summary")
    // TODO: create summary config entity to pass as a parameter
    public List<Integer> generateGameSummary(@PathVariable Long gameId) {
        return gameService.generateGameSummary(gameId);
    }

    // @GetMapping("admin/live-data/{gameId}")
    // Used for admin panel live monitoring
    // Contains: userId, score, generation, answer history [x+xx+++]
    // TODO: PlayerProfile class to store this info, return list of profiles

    // TODO: handle exclusion of a player (delete his data, destroy his cookie, log him out)
}
