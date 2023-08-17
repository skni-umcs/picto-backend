package pl.umcs.workshop.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.umcs.workshop.user.UserInfo;

@RestController
public class RoundController {
    RoundService roundService;

    // TODO change to manual wiring
    @Autowired
    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("round/next/{userId}")
    public Round getNextRound(@PathVariable int userId) {
        return roundService.getNextRound(userId);
    }

    // TODO
    @GetMapping("round/{roundId}/speaker/get")
    public Round getRoundSpeakerInfo(@PathVariable int roundId) {
        return roundService.getRoundSpeakerInfo(roundId);
    }

    // TODO
    @GetMapping("round/{roundId}/listener/get")
    public Round getRoundListenerInfo(@PathVariable int roundId) {
        return roundService.getRoundListenerInfo(roundId);
    }

    // TODO
    @PostMapping("round/speaker/save")
    public Round saveRoundSpeakerInfo(@RequestBody UserInfo userInfo) {
        return roundService.saveRoundSpeakerInfo(userInfo);
    }

    // TODO
    @PostMapping("round/listener/save")
    public Round saveRoundListenerInfo(@RequestBody UserInfo userInfo) {
        return roundService.saveRoundListenerInfo(userInfo);
    }

    @GetMapping("round/{roundId}/result")
    public RoundResult getRoundResult(@PathVariable int roundId) {
        return roundService.getRoundResult(roundId);
    }
}
