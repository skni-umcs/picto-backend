package pl.umcs.workshop.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoundController {
    RoundService roundService;

    @Autowired
    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("/rounds/{gameId}")
    public List<Round> getRounds(@PathVariable String gameId) {
        return roundService.getRounds(Integer.parseInt(gameId));
    }

    public List<Round> saveRounds(int gameId, List<Round> rounds) {
        return roundService.saveRounds(gameId, rounds);
    }

    @GetMapping("/round/{roundId}")
    public Round getRound(@PathVariable String roundId) {
        return roundService.getRound(Integer.parseInt(roundId));
    }

    @PostMapping("/round/{roundId}")
    public Round saveRound(@PathVariable String roundId, Round round) {
        return roundService.saveRound(Integer.parseInt(roundId), round);
    }
}
