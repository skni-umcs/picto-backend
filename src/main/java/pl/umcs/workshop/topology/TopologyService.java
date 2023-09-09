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
import pl.umcs.workshop.utils.Graph;
import pl.umcs.workshop.utils.RoundGenerator;

@Service
public class TopologyService {
  @Autowired private RoundRepository roundRepository;

  public List<Round> generateRoundsForGame(@NotNull Game game, List<User> users) {
    Topology topology = game.getTopology();
    Graph graph =
        Graph.builder()
            .users(users)
            .k(topology.getMaxVertexDegree())
            .p(topology.getProbabilityOfEdgeRedrawing())
            .build();
    graph.generateGraph();

    RoundGenerator roundGenerator =
        RoundGenerator.builder().roundList(new ArrayList<>()).graph(graph).build();
    List<Round> rounds = roundGenerator.generateGenerations(500);

    return roundRepository.saveAll(rounds);
  }
}
