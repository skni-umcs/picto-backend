package pl.umcs.workshop.utils;

import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.user.User;

import java.util.*;

public class RoundGenerator {
    List<Round> roundList = new ArrayList<>();

    public void addNewRound(Long id, User userOne, User userTwo, int generation) {
        Round round = new Round();
        round.setId(id);
        round.setUserOne(userOne);
        round.setUserTwo(userTwo);
        round.setGeneration(generation);
        roundList.add(round);
    }

    public void generateRoundsGeneration(Graph graph, int generation) {
        Set pairedVertices = new HashSet();
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

    public void generateRounds(Graph graph, int numberOfGenerations) {
        for (int i = 0; i < numberOfGenerations; i++) {
            generateRoundsGeneration(graph, i);
        }
    }

    public void printRoundList() {
        for (Round round : roundList) {
            System.out.println(
                    String.format("%d: gen: %d u1: %d u2: %d", round.getId(), round.getGeneration(), round.getUserOne().getId(), round.getUserTwo().getId())
            );
        }
    }
}
