package pl.umcs.workshop.game;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.user.User;

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

  @PostMapping("{gameId}/admin/begin")
  public Game beginGame(@PathVariable Long gameId) throws IOException {
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

  @PostMapping("cookie/join")
  public User joinGameWithCookie(@RequestHeader("x-session") String token) {
    return gameService.joinGameWithCookie(token);
  }

  @PostMapping("{gameId}/admin/end")
  public Game endGame(@PathVariable Long gameId) throws IOException {
    return gameService.endGame(gameId);
  }

  @PostMapping("admin/end/all")
  public void endAllGames() throws IOException {
    gameService.endAllGames();
  }
  
  @GetMapping("{gameId}/admin/summary")
  public String generateGameSummary(@PathVariable Long gameId) {
    return gameService.generateGameSummary(gameId);
  }

  // @GetMapping("admin/live-data/{gameId}")
  // Used for admin panel live monitoring
  // Contains: userId, score, generation, answer history [x+xx+++]
  // TODO: PlayerProfile class to store this info, return list of profiles

  // TODO: handle exclusion of a player (delete his data, destroy his cookie, log him out)
}
