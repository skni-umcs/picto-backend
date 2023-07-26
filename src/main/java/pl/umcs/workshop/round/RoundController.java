package pl.umcs.workshop.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoundController {
    RoundService roundService;

    // TODO change to manual wiring
    @Autowired
    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("/rounds/{gameId}")
    public List<Round> getRounds(@PathVariable int gameId) {
        return roundService.getRounds(gameId);
    }

    public List<Round> saveRounds(int gameId, List<Round> rounds) {
        return roundService.saveRounds(gameId, rounds);
    }

    @GetMapping("/round/{roundId}")
    public Round getRound(@PathVariable int roundId) {
        return roundService.getRound(roundId);
    }

    @PostMapping("/round/{roundId}")
    public Round saveRound(@PathVariable int roundId, Round round) {
        return roundService.saveRound(roundId, round);
    }
}
