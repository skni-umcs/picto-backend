package pl.umcs.workshop.game;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.image.ImageService;
import pl.umcs.workshop.sse.SseService;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;
import pl.umcs.workshop.topology.TopologyService;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.utils.JwtCookieHandler;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopologyRepository topologyRepository;

    @Autowired
    private TopologyService topologyService;

    @Autowired
    private ImageService imageService;

    public Game createGame(@NotNull Game game) {
        if (gameRepository.existsById(game.getTopology().getId())) {
            Topology topology = topologyRepository.findById(game.getTopology().getId()).orElse(null);
            game.setTopology(topology);
        } else {
            Topology topology = Topology.builder()
                    .maxVertexDegree(game.getTopology().getMaxVertexDegree())
                    .probabilityOfEdgeRedrawing(game.getTopology().getProbabilityOfEdgeRedrawing())
                    .build();

            topologyRepository.save(topology);
        }

        return gameRepository.save(game);
    }

    // TODO: begin game method
    public Game beginGame(Long gameId) throws IOException {
        Game game = getGame(gameId);
        List<User> users = userRepository.findAllByGame(game);

        topologyService.generateRoundsForGame(game, users);
        imageService.generateImagesForGame(game);

        SseService.emitEventForAll(gameId, SseService.EventType.GAME_BEGIN);

        return game;
    }

    public User joinGame(Long gameId) {
        Game game = getGame(gameId);

        User user = User.builder()
                .game(game)
                .score(0)
                .generation(0)
                .lastSeen(LocalDateTime.now())
                .build();
        user.setCookie(JwtCookieHandler.createToken(game.getId(), user.getId()));

        User savedUser = userRepository.save(user);
        SseService.addUserSession(gameId, user.getId());

        return savedUser;
    }

    public User joinGameAsUser(Long gameId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Game game = getGame(gameId);

        user.setCookie(JwtCookieHandler.createToken(game.getId(), user.getId()));

        return userRepository.save(user);
    }

    public Game endGame(Long gameId) throws IOException {
        Game game = getGame(gameId);
        game.setEndDateTime(LocalDateTime.now());
        deleteUserCookies(gameId);

        Game savedGame = gameRepository.save(game);
        SseService.emitEventForAll(gameId, SseService.EventType.END_GAME);

        SseService.closeSseConnectionForAll(gameId);

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
