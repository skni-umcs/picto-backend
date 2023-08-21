package pl.umcs.workshop.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.user.UserInfo;

@RestController
@RequestMapping("round/")
public class RoundController {
    RoundService roundService;

    @Autowired
    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("next/{userId}")
    public Round getNextRound(@PathVariable int userId) {
        return roundService.getNextRound(userId);
    }

    // TODO
    @GetMapping("{roundId}/speaker")
    public Round getRoundSpeakerInfo(@PathVariable int roundId) {
        return roundService.getRoundSpeakerInfo(roundId);
    }

    // TODO
    @GetMapping("{roundId}/listener")
    public Round getRoundListenerInfo(@PathVariable int roundId) {
        return roundService.getRoundListenerInfo(roundId);
    }

    // TODO
    @PostMapping("{roundId}/speaker")
    public Round saveRoundSpeakerInfo(@RequestBody UserInfo userInfo) {
        return roundService.saveRoundSpeakerInfo(userInfo);
    }

    // TODO
    @PostMapping("{roundId}/listener")
    public Round saveRoundListenerInfo(@RequestBody UserInfo userInfo) {
        return roundService.saveRoundListenerInfo(userInfo);
    }

    @GetMapping("{roundId}/result")
    public RoundResult getRoundResult(@PathVariable int roundId) {
        return roundService.getRoundResult(roundId);
    }
}
