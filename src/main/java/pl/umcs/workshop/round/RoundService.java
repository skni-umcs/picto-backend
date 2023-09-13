package pl.umcs.workshop.round;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.user.UserService;

@Service
public class RoundService {
  @Autowired private RoundRepository roundRepository;

  @Autowired private ImageRepository imageRepository;

  @Autowired private UserService userService;

  @Autowired private GameService gameService;

  @Autowired private SymbolRepository symbolRepository;

  @Autowired private UserRepository userRepository;

  public Round getNextRound(Long userId) throws IOException {
    // Check what generation the user is on
    User user = userService.getUser(userId);
    Game game = gameService.getGame(user.getGame().getId());
    Round round =
        roundRepository.getNextRound(user.getGame().getId(), userId, user.getGeneration() + 1);

    if (round == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Round not found");
    }

    user.setGeneration(user.getGeneration() + 1);
    userRepository.save(user);

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

    List<Symbol> selectedSymbols = new ArrayList<>(round.getSymbols());
    return Collections.singletonList(selectedSymbols);
  }

  public Round saveRoundSpeakerInfo(@NotNull UserInfo userInfo) throws IOException {
    Round round = getRound(userInfo.getRoundId());
    round.setUserOneAnswerTime(userInfo.getAnswerTime());

    User speaker = userService.getUser(userInfo.getUserId());
    speaker.setLastSeen(LocalDateTime.now());
    userRepository.save(speaker);

    List<Symbol> selectedSymbolIds = userInfo.getSymbolsSelected();
    Set<Symbol> symbolsSelected = new HashSet<>();
    for (Symbol symbol : selectedSymbolIds) {
      Symbol symbolData = symbolRepository.findById(symbol.getId()).orElse(null);

      if (symbolData == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Symbol not found");
      }

      symbolsSelected.add(symbolData);
    }

    User listener = round.getUserTwo();
    round.setSymbols(symbolsSelected);
    Round saveRound = roundRepository.save(round);

    SseService.emitEventForUser(speaker, SseService.EventType.SPEAKER_HOLD);
    SseService.emitEventForUser(listener, SseService.EventType.LISTENER_READY);

    return saveRound;
  }

  public Round saveRoundListenerInfo(@NotNull UserInfo userInfo) {
    Round round = getRound(userInfo.getRoundId());

    round.setUserTwoAnswerTime(userInfo.getAnswerTime());
    round.setImageSelected(userInfo.getImageSelected());

    User listener = userService.getUser(userInfo.getUserId());
    userService.updateUserLastSeen(userInfo.getUserId());

    User speaker = round.getUserOne();
    Round saveRound = roundRepository.save(round);

    try {
      SseService.emitEventForUser(speaker, SseService.EventType.RESULT_READY);
      SseService.emitEventForUser(listener, SseService.EventType.RESULT_READY);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return saveRound;
  }

  public RoundResult getRoundResult(Long roundId, Long userId) {
    Round round = getRound(roundId);
    Game game = gameService.getGame(round.getGame().getId());
    User user = userService.getUser(userId);

    // Multithreading
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(
        () -> {
          try {
            SseService.emitEventForUser(user, SseService.EventType.AWAITING_ROUND);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        },
        5,
        TimeUnit.SECONDS);

    // Return result
    if (isImageCorrect(round)) {
      user.setScore(user.getScore() + game.getCorrectAnswerPoints());
      userRepository.save(user);

      return new RoundResult(RoundResult.Result.CORRECT, game.getCorrectAnswerPoints());
    }

    user.setScore(user.getScore() + game.getWrongAnswerPoints());
    userRepository.save(user);

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
