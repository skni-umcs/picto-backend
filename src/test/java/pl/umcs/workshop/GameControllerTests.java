package pl.umcs.workshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.game.GameRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Test
    @Order(1)
    public void createGameShouldReturnGame() throws Exception {
        Game game = Game.builder()
                .userOneNumberOfImages(4)
                .userTwoNumberOfImages(4)
                .userOneTime(5)
                .userTwoTime(3)
                .symbolGroupsAmount(3)
                .symbolsInGroupAmount(4)
                .correctAnswerPoints(1)
                .wrongAnswerPoints(-1)
                .topologyId(1)
                .createDateTime(LocalDateTime.now())
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders
                    .post("/game/create")
                    .content(asJsonString(game))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                    .jsonPath("$.id")
                    .exists());
    }

    // TODO: FIX TESTS
    @Test
    @Order(2)
    public void joinGameShouldReturnUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/game/join/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.id")
                        .value(1));
    }

    @Test
    @Order(3)
    public void endGameShouldReturnGame() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/game/end/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.id")
                        .value(1));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
