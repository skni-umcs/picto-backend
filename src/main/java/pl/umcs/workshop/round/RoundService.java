package pl.umcs.workshop.round;

import java.io.IOException;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameService;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.image.ImageRepository;
import pl.umcs.workshop.sse.SseService;
import pl.umcs.workshop.symbol.Symbol;
import pl.umcs.workshop.symbol.SymbolRepository;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserInfo;
import pl.umcs.workshop.user.UserService;

@Service
public class RoundService {
  @Autowired private RoundRepository roundRepository;

  @Autowired private ImageRepository imageRepository;

  @Autowired private UserService userService;

  @Autowired private GameService gameService;

  @Autowired private SymbolRepository symbolRepository;

  public Round getNextRound(Long userId) throws IOException {
    // Check what generation the user is on
    User user = userService.getUser(userId);
    Game game = gameService.getGame(user.getGame().getId());
    Round round =
        roundRepository.getNextRound(user.getGame().getId(), userId, user.getGeneration() + 1);

    SseService.EventType eventType;
    if (Objects.equals(round.getUserOne().getId(), userId)) {
      eventType = SseService.EventType.SPEAKER_READY;
    } else {
      eventType = SseService.EventType.LISTENER_HOLD;
    }
    SseService.emitEventForUser(user, eventType);

    return round;
  }

  public List<Image> getImages(Long roundId, Long userId) {
    return imageRepository.findAllImagesForUser(roundId, userId);
  }

  public List<List<Symbol>> getSymbols(Long roundId, Long userId) {
    Round round = getRound(roundId);
    List<Symbol> symbols = symbolRepository.findAllByGameId(round.getGame().getId());

    if (Objects.equals(round.getUserOne().getId(), userId)) {
      List<List<Symbol>> symbolMatrix = new ArrayList<>();
      Set<Long> groupIds = new HashSet<>();

      for (Symbol symbol : symbols) {
        Long groupId = symbol.getGroup().getId();
        groupIds.add(groupId);
      }

      for (Long id : groupIds) {
        symbolMatrix.add(symbolRepository.findAllByRoundsIdAndGroupId(roundId, id));
      }

      return symbolMatrix;
    }

    return Collections.singletonList(symbolRepository.findAllByUsersId(roundId));
  }

  public Round saveRoundSpeakerInfo(@NotNull UserInfo userInfo) throws IOException {
    Round round = getRound(userInfo.getRoundId());
    round.setUserOneAnswerTime(userInfo.getAnswerTime());

    User speaker = userService.getUser(userInfo.getUserId());
    userService.updateUserLastSeen(userInfo.getUserId());

    User listener = round.getUserTwo();

    Round saveRound = roundRepository.save(round);

    SseService.emitEventForUser(speaker, SseService.EventType.SPEAKER_HOLD);
    SseService.emitEventForUser(listener, SseService.EventType.LISTENER_READY);

    return saveRound;
  }

  public Round saveRoundListenerInfo(@NotNull UserInfo userInfo) {
    Round round = getRound(userInfo.getRoundId());

    round.setUserTwoAnswerTime(userInfo.getAnswerTime());
    round.setImageSelected(userInfo.getImageSelected());

    User user = userService.getUser(userInfo.getUserId());
    userService.updateUserLastSeen(userInfo.getUserId());

    Round saveRound = roundRepository.save(round);

    try {
      SseService.emitEventForUser(user, SseService.EventType.RESULT_READY);
      SseService.emitEventForUser(user, SseService.EventType.RESULT_READY);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return saveRound;
  }

  public RoundResult getRoundResult(Long roundId) throws IOException {
    Round round = getRound(roundId);
    Game game = gameService.getGame(round.getGame().getId());

    User userOne = userService.getUser(round.getUserOne().getId());
    User userTwo = userService.getUser(round.getUserTwo().getId());

    SseService.emitEventForUser(userOne, SseService.EventType.AWAITING_ROUND);
    SseService.emitEventForUser(userTwo, SseService.EventType.AWAITING_ROUND);

    if (isImageCorrect(round)) {
      return new RoundResult(RoundResult.Result.CORRECT, game.getCorrectAnswerPoints());
    }

    return new RoundResult(RoundResult.Result.WRONG, game.getWrongAnswerPoints());
  }

  private @NotNull Round getRound(Long roundId) {
    Round round = roundRepository.findById(roundId).orElse(null);

    if (round == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Round not found");
    }

    return round;
  }

  public boolean isImageCorrect(@NotNull Round round) {
    return Objects.equals(round.getImageSelected(), round.getTopic());
  }
}
