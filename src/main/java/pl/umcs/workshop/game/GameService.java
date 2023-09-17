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
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.image.ImageService;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
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

  @Autowired private RoundRepository roundRepository;

  public Game createGame(@NotNull Game game) {
    List<Image> images = imageService.getAllImagesAndGroups();
    if (images.isEmpty()) {
      imageService.addImages();
      imageService.addSymbols();
    }

    if (topologyRepository.existsById(game.getTopology().getId())) {
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
    game.setCreateDateTime(LocalDateTime.now());

    return gameRepository.save(game);
  }

  public Game beginGame(Long gameId) throws IOException {
    Game game = getGame(gameId);
    List<User> users = userRepository.findAllByGame(game);

    topologyService.generateRoundsForGame(game, users);
    imageService.generateImagesForGame(game);

    // TODO: fix this to get symbols from config
    List<Symbol> symbols = symbolRepository.findAllByGamesId(gameId);
    game.setSymbols(new HashSet<>(symbols));
    for (Symbol symbol : symbols) {
      symbol.getGames().add(game);
      symbol.getRounds().addAll(game.getRounds());
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
    savedUser = userRepository.save(user);

    SseService.addUserSession(user);

    return savedUser;
  }

  public User joinGameAsUser(Long gameId, Long userId) {
    User user = userService.getUser(userId);
    getGame(gameId);

    return user;
  }

  public User joinGameWithCookie(String token) {
    Long userId = JwtCookieHandler.getUserId(token);
    Long gameId = JwtCookieHandler.getGameId(token);

    return joinGameAsUser(gameId, userId);
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

  public void endAllGames() throws IOException {
    List<Game> games = gameRepository.findAll();

    for (Game game : games) {
      if (game.getEndDateTime() == null) {
        endGame(game.getId());
      }
    }

    gameRepository.saveAll(games);
  }

  public String generateGameSummary(Long gameId) {
    Game game = getGame(gameId);
    StringBuilder data = new StringBuilder("generation,score\n");

    for (int generation = 1; generation <= game.getNumberOfGenerations(); generation++) {
      List<Round> rounds = roundRepository.findAllByGameIdAndGeneration(gameId, generation);

      int correct = 0;
      int total = 0;
      for (Round round : rounds) {
        total++;

        if (round.getTopic() == round.getImageSelected()) {
          correct++;
        }
      }

      double score = (double) correct / total;
      data.append(String.format("%d,%.1f\n", generation, score * 100));
    }

    return data.toString();
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
