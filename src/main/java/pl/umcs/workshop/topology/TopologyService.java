package pl.umcs.workshop.topology;

import java.util.*;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.graph.Graph;
import pl.umcs.workshop.graph.GraphEntity;
import pl.umcs.workshop.graph.GraphRepository;
import pl.umcs.workshop.utils.RoundGenerator;

@Service
public class TopologyService {
  @Autowired private RoundRepository roundRepository;
  @Autowired private GraphRepository graphRepository;

  public List<Round> generateRoundsForGame(@NotNull Game game, List<User> users) {
    Topology topology = game.getTopology();
    Graph graph =
        Graph.builder()
            .users(users)
            .k(topology.getMaxVertexDegree())
            .p(topology.getProbabilityOfEdgeRedrawing())
            .build();
    List<Map.Entry<User, User>> edges = graph.generateGraph();

    // Save graph to db
    List<GraphEntity> graphToSave = new ArrayList<>();
    for (Map.Entry<User, User> edge : edges) {
      graphToSave.add(
          GraphEntity.builder()
              .gameId(game.getId())
              .userOneId(edge.getKey().getId())
              .userTwoId(edge.getValue().getId())
              .build());
    }
    graphRepository.saveAll(graphToSave);

    RoundGenerator roundGenerator =
        RoundGenerator.builder().roundList(new ArrayList<>()).graph(graph).game(game).build();
    List<Round> rounds = roundGenerator.generateGenerations(game.getNumberOfGenerations());

    return roundRepository.saveAll(rounds);
  }
}
