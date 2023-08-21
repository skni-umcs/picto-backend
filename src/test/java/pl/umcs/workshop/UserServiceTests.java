package pl.umcs.workshop;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.user.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1)
                .gameId(1)
                .score(11)
                .lastSeen(LocalDateTime.now())
                .cookie(new Cookie("cookieOne", "valueOfCookieOne"))
                .build();
    }

    @Test
    public void givenUserId_whenGetUser_thenReturnUserObject() {
        // given
        given(userRepository.findById(1)).willReturn(Optional.of(user));

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

        // TODO: make this unique or figure out how we want to update users
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
        willDoNothing().given(userRepository).deleteById(1);

        // when
        userService.deleteUser(user.getId());

        // then
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}
