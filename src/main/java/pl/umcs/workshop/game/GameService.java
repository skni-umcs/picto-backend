package pl.umcs.workshop.game;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;
import pl.umcs.workshop.topology.TopologyService;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;

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
    public List<Integer> beginGame(int gameId) {
        // Get all users for given game
        List<User> users = userRepository.findAllByGameId(gameId);

        // Get game config
        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Generate brackets (generations and rounds) based on topology (topologyId)
        Topology topology = topologyRepository.findById(game.getTopologyId()).orElse(null);

        if (topology == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topology not found");
        }

        return topologyService.generateBrackets(users);
    }

    public User joinGame(int gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getEndDateTime() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game has ended");
        }

        Cookie cookie = new Cookie("ThisIsACookie", "eh");

        User user = User.builder()
                .gameId(gameId)
                .score(0)
                .lastSeen(LocalDateTime.now())
                .cookie(cookie)
                .build();

        return userRepository.save(user);
    }

    public Game endGame(int gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        game.setEndDateTime(LocalDateTime.now());

        List<User> users = userRepository.findAllByGameId(gameId);
        for (User user : users) {
            user.setCookie(null);
        }

        userRepository.saveAll(users);

        return gameRepository.save(game);
    }

    // TODO: generate game summary method
    public List<Integer> generateGameSummary(int gameId) {
        // Summarize data from the game using config given as the parameter
        // (e.g. number of generations, players to exclude etc.)
        // Generate points to use in chart

        return null;
    }
}
