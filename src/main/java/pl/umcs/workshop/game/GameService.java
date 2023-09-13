package pl.umcs.workshop.game;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.group.Group;
import pl.umcs.workshop.group.GroupRepository;
import pl.umcs.workshop.image.ImageService;
import pl.umcs.workshop.sse.SseService;
import pl.umcs.workshop.symbol.Symbol;
import pl.umcs.workshop.symbol.SymbolRepository;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;
import pl.umcs.workshop.topology.TopologyService;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.user.UserService;
import pl.umcs.workshop.utils.JwtCookieHandler;

@Service
public class GameService {
  @Autowired private GameRepository gameRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private TopologyRepository topologyRepository;

  @Autowired private UserService userService;

  @Autowired private TopologyService topologyService;

  @Autowired private ImageService imageService;

  @Autowired private GroupRepository groupRepository;

  @Autowired private SymbolRepository symbolRepository;

  public Game createGame(@NotNull Game game) {
    if (gameRepository.existsById(game.getTopology().getId())) {
      Topology topology = topologyRepository.findById(game.getTopology().getId()).orElse(null);
      game.setTopology(topology);
    } else {
      Topology topology =
          Topology.builder()
              .maxVertexDegree(game.getTopology().getMaxVertexDegree())
              .probabilityOfEdgeRedrawing(game.getTopology().getProbabilityOfEdgeRedrawing())
              .build();

      topologyRepository.save(topology);
      game.setTopology(topology);
    }
    Group group = groupRepository.findById(game.getGroup().getId()).orElse(null);
    game.setGroup(group);

    return gameRepository.save(game);
  }

  // TODO: begin game method
  public Game beginGame(Long gameId) throws IOException {
    Game game = getGame(gameId);
    List<User> users = userRepository.findAllByGame(game);

    topologyService.generateRoundsForGame(game, users);
    imageService.generateImagesForGame(game);

    // TODO: fix this to get symbols from config
    List<Symbol> symbols = symbolRepository.findAll();
    game.setSymbols(new HashSet<>(symbols));
    for (Symbol symbol : symbols) {
      symbol.setGame(new HashSet<>(List.of(game)));
      symbol.setRounds(new HashSet<>(game.getRounds()));
    }
    symbolRepository.saveAll(symbols);
    gameRepository.save(game);

    SseService.emitEventForAll(game.getId(), SseService.EventType.GAME_BEGIN);

    return game;
  }

  public User joinGame(Long gameId) {
    Game game = getGame(gameId);

    User user =
        User.builder().game(game).score(0).generation(0).lastSeen(LocalDateTime.now()).build();

    User savedUser = userRepository.save(user);
    user.setCookie(JwtCookieHandler.createToken(game.getId(), savedUser.getId()));

    SseService.addUserSession(user);

    return savedUser;
  }

  public User joinGameAsUser(Long gameId, Long userId) {
    User user = userService.getUser(userId);

    Game game = getGame(gameId);
    User savedUser = userRepository.save(user);

    user.setCookie(JwtCookieHandler.createToken(game.getId(), savedUser.getId()));

    return savedUser;
  }

  public Game endGame(Long gameId) throws IOException {
    Game game = getGame(gameId);
    game.setEndDateTime(LocalDateTime.now());
    deleteUserCookies(gameId);

    Game savedGame = gameRepository.save(game);
    SseService.emitEventForAll(savedGame.getId(), SseService.EventType.END_GAME);

    SseService.closeSseConnectionForAll(savedGame.getId());

    return savedGame;
  }

  // TODO
  public List<Integer> generateGameSummary(Long gameId) {
    // Summarize data from the game using config given as the parameter
    // (e.g. number of generations, players to exclude etc.)
    // Generate points to use in chart

    return null;
  }

  @NotNull
  public Game getGame(Long gameId) {
    Game game = gameRepository.findById(gameId).orElse(null);

    if (game == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
    }

    if (game.getEndDateTime() != null) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game has ended");
    }

    return game;
  }

  public @NotNull List<User> deleteUserCookies(Long gameId) {
    List<User> users = userRepository.findAllByGameId(gameId);
    for (User user : users) {
      user.setCookie(null);
    }

    return userRepository.saveAll(users);
  }
}
