package pl.umcs.workshop.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoundController {
    RoundService roundService;

    // TODO change to manual wiring
    @Autowired
    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("round/{roundId}/speaker")
    public Round getRoundSpeakerInfo(@PathVariable int roundId) {
        return roundService.getRoundSpeakerInfo(roundId);
    }

    @GetMapping("round/{roundId}/listener")
    public Round getRoundListenerInfo(@PathVariable int roundId) {
        return roundService.getRoundListenerInfo(roundId);
    }

    // TODO what should these return
    @PostMapping("round/{roundId}/speaker")
    public Round saveRoundSpeakerInfo(@PathVariable int roundId) {
        return roundService.saveRoundSpeakerInfo(roundId);
    }

    @PostMapping("round/{roundId}/listener")
    public Round saveRoundListenerInfo(@PathVariable int roundId) {
        return roundService.saveRoundListenerInfo(roundId);
    }

    @GetMapping("round/result/{roundId}")
    // true if got a point, false otherwise
    // TODO check if this should return points gotten
    public boolean getRoundResult(@PathVariable int roundId) {
        return roundService.getRoundResult(roundId);
    }
}
