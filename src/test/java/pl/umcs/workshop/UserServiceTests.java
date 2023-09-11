package pl.umcs.workshop;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.user.UserService;
import pl.umcs.workshop.utils.JwtCookieHandler;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
  public static Game game;
  private static User user;
  private static Topology topology;
  @Mock private UserRepository userRepository;
  @InjectMocks private UserService userService;

  @BeforeAll
  public static void setup() {
    topology = Topology.builder().maxVertexDegree(5).probabilityOfEdgeRedrawing(0.55).build();

    game =
        Game.builder()
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

    user =
        User.builder()
            .id(1L)
            .game(game)
            .score(11)
            .lastSeen(LocalDateTime.now())
            .cookie(JwtCookieHandler.createToken(1L, 1L))
            .build();
  }

  @Test
  public void givenUserId_whenGetUser_thenReturnUserObject() {
    // given
    given(userRepository.findById(1L)).willReturn(Optional.of(user));

    // when
    User foundUser = userService.getUser(user.getId());

    // then
    Assertions.assertThat(foundUser).isNotNull();
    Assertions.assertThat(foundUser.getId()).isEqualTo(user.getId());
  }

  @Test
  public void givenUserObjectAndId_whenUpdateUser_thenReturnUserObject() {
    // given
    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(userRepository.save(user)).willReturn(user);

    User userToUpdate = user;

    // when
    User updatedUser = userService.updateUser(user.getId(), userToUpdate);

    // then
    Assertions.assertThat(updatedUser).isNotNull();
    Assertions.assertThat(updatedUser.getId()).isEqualTo(user.getId());
  }

  @Test
  public void givenUserId_whenRemoveUser_thenNothing() {
    // given
    willDoNothing().given(userRepository).deleteById(1L);

    // when
    userService.deleteUser(user.getId());

    // then
    verify(userRepository, times(1)).deleteById(user.getId());
  }
}
