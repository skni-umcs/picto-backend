package pl.umcs.workshop;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.graph.Graph;
import pl.umcs.workshop.utils.JwtCookieHandler;

@ExtendWith(MockitoExtension.class)
public class GraphTests {

  private Graph graph;
  private User user1;
  private User user2;

  @BeforeEach
  public void setup() {
    user1 =
        User.builder()
            .id(1L)
            .game(mock(Game.class))
            .score(6)
            .generation(1)
            .lastSeen(LocalDateTime.now())
            .cookie(JwtCookieHandler.createToken(1L, 1L))
            .build();
    user2 =
        User.builder()
            .id(2L)
            .game(mock(Game.class))
            .score(4)
            .generation(1)
            .lastSeen(LocalDateTime.now())
            .cookie(JwtCookieHandler.createToken(1L, 2L))
            .build();
  }

  @Test
  void shouldGenerateGraph() {
    // Given
    int k = 2;
    double p = 0;
    List<User> users = Arrays.asList(user1, user2);
    graph = Graph.builder().users(users).k(k).p(p).build();

    // When
    List<Map.Entry<User, User>> edges = graph.generateGraph();

    // Then
    assertFalse(edges.isEmpty());
  }

  @Test
  void shouldReGenerateEdges() {
    // Given
    int k = 2;
    double p = 1; // Making it 100% sure to regenerate edges
    List<User> users = Arrays.asList(user1, user2);
    graph = Graph.builder().users(users).k(k).p(p).build();

    // When
    List<Map.Entry<User, User>> edges = graph.generateGraph();

    // Then
    assertFalse(edges.isEmpty());
  }

  @Test
  void shouldNotReGenerateEdges() {
    // Given
    int k = 0; // No edges will be added
    double p = 1;
    List<User> users = Arrays.asList(user1, user2);
    graph = Graph.builder().users(users).k(k).p(p).build();

    // When
    List<Map.Entry<User, User>> edges = graph.generateGraph();

    // Then
    assertTrue(edges.isEmpty());
  }

  @Test
  void shouldReturnAdjVertices() {
    // Given
    int k = 1;
    double p = 0;
    List<User> users = Arrays.asList(user1, user2);
    graph = Graph.builder().users(users).k(k).p(p).build();
    graph.generateGraph();

    // When
    List<User> adjVerticesUser1 = graph.getAdjVertices(user1);
    List<User> adjVerticesUser2 = graph.getAdjVertices(user2);

    // Then
    assertFalse(adjVerticesUser1.isEmpty());
    assertTrue(
        adjVerticesUser1.contains(user2)); // As we have only two users they should be connected

    assertFalse(adjVerticesUser2.isEmpty());
    assertTrue(adjVerticesUser2.contains(user1)); // Same here
  }
}
