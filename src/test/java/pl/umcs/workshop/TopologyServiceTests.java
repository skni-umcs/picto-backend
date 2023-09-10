package pl.umcs.workshop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyService;
import pl.umcs.workshop.user.User;

@ExtendWith(MockitoExtension.class)
class TopologyServiceTests {
  @Mock private RoundRepository roundRepository;
  @InjectMocks private TopologyService topologyService;

  @Test
  void shouldGenerateAndSaveRoundsForGameAndUsers() {
    // Given
    Game game = mock(Game.class);
    List<User> users = Arrays.asList(mock(User.class), mock(User.class));
    Topology topology = Topology.builder().maxVertexDegree(3).probabilityOfEdgeRedrawing(0.25).build();
    given(game.getTopology()).willReturn(topology);
    List<Round> expectedRounds = Collections.singletonList(mock(Round.class));
    given(roundRepository.saveAll(any())).willReturn(expectedRounds);

    // When
    List<Round> actualRounds = topologyService.generateRoundsForGame(game, users);

    // Then
    verify(roundRepository).saveAll(any());
    assertEquals(expectedRounds, actualRounds);
  }
}
