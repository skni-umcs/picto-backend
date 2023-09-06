package pl.umcs.workshop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyService;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.utils.JwtCookieHandler;

@ExtendWith(MockitoExtension.class)
public class TopologyServiceTests {
    private static final List<User> users = new ArrayList<>();

    private static Topology topology;

    @InjectMocks
    private static TopologyService topologyService;

    @BeforeAll
    public static void setup() {
        topology = Topology.builder()
                .maxVertexDegree(2)
                .probabilityOfEdgeRedrawing(0.25)
                .build();

        Game game = Game.builder()
                .id(1L)
                .userOneNumberOfImages(4)
                .userTwoNumberOfImages(4)
                .userOneTime(5)
                .userTwoTime(3)
                .symbolGroupsAmount(3)
                .symbolsInGroupAmount(4)
                .correctAnswerPoints(1)
                .wrongAnswerPoints(-1)
                .topology(topology)
                .createDateTime(LocalDateTime.now())
                .build();

        User userOne = User.builder()
            .id(1L)
            .game(game)
            .score(11)
            .lastSeen(LocalDateTime.now())
            .cookie(JwtCookieHandler.createToken(1L, 1L))
            .build();

        User userTwo = User.builder()
                .id(2L)
                .game(game)
                .score(11)
                .lastSeen(LocalDateTime.now())
                .cookie(JwtCookieHandler.createToken(1L, 2L))
                .build();

        User userThree = User.builder()
                .id(3L)
                .game(game)
                .score(11)
                .lastSeen(LocalDateTime.now())
                .cookie(JwtCookieHandler.createToken(1L, 3L))
                .build();

        users.addAll(Arrays.asList(userOne, userTwo, userThree));
    }

//    @Test
//    public void givenUsersListAndTopologyObject_whenGenerateBrackets_thenNothing() {
//        // given
//
//        // when
//        topologyService.generateBrackets(users, topology);
//
//        // then
//    }
}
