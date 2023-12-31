// package pl.umcs.workshop;
//
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import java.time.LocalDateTime;
// import org.junit.Test;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Order;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import pl.umcs.workshop.game.Game;
// import pl.umcs.workshop.topology.Topology;
//
// @RunWith(SpringRunner.class)
// @SpringBootTest
// @AutoConfigureMockMvc
// public class GameControllerTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    private Topology topology;
//
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().registerModule(new
// JavaTimeModule()).writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @BeforeEach
//    public void setup() {
//        topology = Topology.builder()
//                .maxVertexDegree(5)
//                .probabilityOfEdgeRedrawing(0.55)
//                .build();
//    }
//
////    @Test
////    @Order(1)
////    public void createGameShouldReturnGame() throws Exception {
////        Game game = Game.builder()
////                .userOneNumberOfImages(4)
////                .userTwoNumberOfImages(4)
////                .userOneTime(5)
////                .userTwoTime(3)
////                .symbolGroupsAmount(3)
////                .symbolsInGroupAmount(4)
////                .correctAnswerPoints(1)
////                .wrongAnswerPoints(-1)
////                .topology(topology)
////                .createDateTime(LocalDateTime.now())
////                .build();
////
////        this.mockMvc.perform(MockMvcRequestBuilders
////                        .post("/game/admin/create")
////                        .content(asJsonString(game))
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .accept(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk())
////                .andExpect(MockMvcResultMatchers
////                        .jsonPath("$.id")
////                        .exists());
////    }
// }
