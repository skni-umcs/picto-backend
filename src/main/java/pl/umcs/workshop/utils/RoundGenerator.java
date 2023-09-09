package pl.umcs.workshop.utils;

import java.util.*;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.user.User;

@Builder
@Getter
@Setter
public class RoundGenerator {
  private List<Round> roundList;
  private Graph graph;
  private Game game;

  public void addNewRound(Long id, User userOne, User userTwo, int generation) {
    Round round =
        Round.builder()
            .id(id)
            .game(game)
            .userOne(userOne)
            .userTwo(userTwo)
            .generation(generation)
            .build();

    roundList.add(round);
  }

  public void generateRounds(int generation) {
    Set<User> pairedVertices = new HashSet<>();

    for (Map.Entry<User, User> edge : graph.getEdges()) {
      User userOne = edge.getKey();
      User userTwo = edge.getValue();

      if (!pairedVertices.contains(userOne) && !pairedVertices.contains(userTwo)) {
        pairedVertices.add(userOne);
        pairedVertices.add(userTwo);
        addNewRound((long) roundList.size(), userOne, userTwo, generation);
      }
    }
  }

  public List<Round> generateGenerations(int numberOfGenerations) {
    for (int generation = 1; generation <= numberOfGenerations; generation++) {
      generateRounds(generation);
    }

    return roundList;
  }

  public void printRoundList() {
    for (Round round : roundList) {
      System.out.printf(
          "%d: gen: %d u1: %d u2: %d%n",
          round.getId(),
          round.getGeneration(),
          round.getUserOne().getId(),
          round.getUserTwo().getId());
    }
  }
}
