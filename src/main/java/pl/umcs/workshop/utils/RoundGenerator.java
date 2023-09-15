package pl.umcs.workshop.utils;

import java.util.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.graph.Graph;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.user.User;

@Builder
@Getter
@Setter
public class RoundGenerator {
  private List<Round> roundList;
  private Graph graph;
  private Game game;

  public void addNewRound(User userOne, User userTwo, int generation) {
    Round round =
        Round.builder().game(game).userOne(userOne).userTwo(userTwo).generation(generation).build();

    roundList.add(round);
  }

  public Map.Entry<User, User> getRandomEdge(@NotNull List<Map.Entry<User, User>> edges) {
    Random random = new Random();
    int randomIndex = random.nextInt(edges.size());
    return edges.get(randomIndex);
  }

  public void removeAllEdgesWithUser(@NotNull List<Map.Entry<User, User>> edges, User user) {
    edges.removeIf(edge -> edge.getKey().equals(user) || edge.getValue().equals(user));
  }

  public Map.Entry<User, User> randomPair(User candidateOne, User candidateTwo) {
    Random random = new Random();
    int whichUser = random.nextInt(2);
    if (whichUser == 0) {
      return new AbstractMap.SimpleEntry<>(candidateOne, candidateTwo);
    } else {
      return new AbstractMap.SimpleEntry<>(candidateTwo, candidateOne);
    }
  }

  public void generateRounds(int generation, User noPairUser) {
    List<Map.Entry<User, User>> edgesLeft = new ArrayList<>(graph.getEdges());
    Set<User> usersWithoutRounds = new HashSet<>(game.getUsers());

    if(noPairUser != null) {
      usersWithoutRounds.remove(noPairUser);
      removeAllEdgesWithUser(edgesLeft, noPairUser);
    }

    while (!edgesLeft.isEmpty()) {
      Map.Entry<User, User> randomEdge = getRandomEdge(edgesLeft);
      User candidateOne = randomEdge.getKey();
      User candidateTwo = randomEdge.getValue();

      Map.Entry<User, User> newPair = randomPair(candidateOne, candidateTwo);
      User userOne = newPair.getKey();
      User userTwo = newPair.getValue();

      addNewRound(userOne, userTwo, generation);
      removeAllEdgesWithUser(edgesLeft, userOne);
      removeAllEdgesWithUser(edgesLeft, userTwo);

      usersWithoutRounds.remove(userOne);
      usersWithoutRounds.remove(userTwo);
    }

    List<User> orderedUsersWithoutRounds = new ArrayList<>(usersWithoutRounds);
    Collections.shuffle(orderedUsersWithoutRounds);
    while (orderedUsersWithoutRounds.size() > 1) {
      User userOne = orderedUsersWithoutRounds.get(0);
      User userTwo = orderedUsersWithoutRounds.get(1);
      addNewRound(userOne, userTwo, generation);

      // Has to be this order, otherwise IndexOutOfBoundsException may occur
      orderedUsersWithoutRounds.remove(1);
      orderedUsersWithoutRounds.remove(0);
    }
  }

  public List<Round> generateGenerations(int numberOfGenerations) {
    List<User> users = new ArrayList<>(game.getUsers());
    for (int generation = 1; generation <= numberOfGenerations; generation++) {
        User noPairUser = null;
        if(users.size()%2 == 1) {
            noPairUser = users.get(generation%users.size());
        }
        generateRounds(generation, noPairUser);
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
