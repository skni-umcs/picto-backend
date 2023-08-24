package pl.umcs.workshop.game;

import jakarta.servlet.http.Cookie;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;
import pl.umcs.workshop.topology.TopologyService;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.utils.JWTCookieHandler;

import java.time.LocalDateTime;
import java.util.List;

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

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    // TODO: begin game method
    public Game beginGame(Long gameId) {
        // Get all users for given game
        List<User> users = userRepository.findAllByGameId(gameId);

        // Get game config
        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // TODO: if topology(topology) is not present, use p and k from config to generate new topology (and return id)
        // Generate brackets (generations and rounds) based on topology (topology(topology))
        Topology topology = topologyRepository.findById(game.getTopology().getId()).orElse(null);

        if (topology == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topology not found");
        }

        // return Game object with topology info present (saved game with this info)
        return null;
    }

    public User joinGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getEndDateTime() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game has ended");
        }

        User user = User.builder()
                .game(game)
                .score(0)
                .generation(0)
                .lastSeen(LocalDateTime.now())
                .build();

        user.setCookie(JWTCookieHandler.createToken(game.getId(), user.getId()));

        return userRepository.save(user);
    }

    public User joinGameAsUser(Long gameId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getEndDateTime() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game has ended");
        }

        user.setCookie(JWTCookieHandler.createToken(game.getId(), user.getId()));

        return userRepository.save(user);
    }

    public Game endGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getEndDateTime() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game has already ended");
        }
        game.setEndDateTime(LocalDateTime.now());

        deleteUserCookies(gameId);

        return gameRepository.save(game);
    }

    // TODO: generate game summary method
    public List<Integer> generateGameSummary(Long gameId) {
        // Summarize data from the game using config given as the parameter
        // (e.g. number of generations, players to exclude etc.)
        // Generate points to use in chart

        return null;
    }

    public @NotNull List<User> deleteUserCookies(Long gameId) {
        List<User> users = userRepository.findAllByGameId(gameId);
        for (User user : users) {
            user.setCookie(null);
        }

        return userRepository.saveAll(users);
    }
}
